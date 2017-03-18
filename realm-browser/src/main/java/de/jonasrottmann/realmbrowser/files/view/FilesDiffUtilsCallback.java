package de.jonasrottmann.realmbrowser.files.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.util.DiffUtil;
import de.jonasrottmann.realmbrowser.files.model.FilesPojo;
import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesDiffUtilsCallback extends DiffUtil.Callback {

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_SIZE = "KEY_SIZE";

    private List<FilesPojo> mOldList;
    private List<FilesPojo> mNewList;

    FilesDiffUtilsCallback(List<FilesPojo> oldList, List<FilesPojo> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).getName().equals(mOldList.get(oldItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        FilesPojo newProduct = mNewList.get(newItemPosition);
        FilesPojo oldProduct = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newProduct.getName().equals(oldProduct.getName())) {
            diffBundle.putString(KEY_NAME, newProduct.getName());
        }
        if (newProduct.getSizeInByte() != oldProduct.getSizeInByte()) {
            diffBundle.putString(KEY_SIZE, newProduct.getSize());
        }
        if (diffBundle.size() == 0) {
            return null;
        }
        return diffBundle;
    }
}
