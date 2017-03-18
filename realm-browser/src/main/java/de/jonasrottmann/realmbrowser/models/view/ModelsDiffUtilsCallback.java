package de.jonasrottmann.realmbrowser.models.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v7.util.DiffUtil;
import de.jonasrottmann.realmbrowser.models.model.ModelPojo;
import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsDiffUtilsCallback extends DiffUtil.Callback {

    public static final String KEY_CLASS = "KEY_CLASS";
    public static final String KEY_COUNT = "KEY_COUNT";

    private List<ModelPojo> mOldList;
    private List<ModelPojo> mNewList;

    ModelsDiffUtilsCallback(List<ModelPojo> oldList, List<ModelPojo> newList) {
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
        return mNewList.get(newItemPosition).getKlass().equals(mOldList.get(oldItemPosition).getKlass());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        ModelPojo newProduct = mNewList.get(newItemPosition);
        ModelPojo oldProduct = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newProduct.getKlass().equals(oldProduct.getKlass())) {
            diffBundle.putSerializable(KEY_CLASS, newProduct.getKlass());
        }
        if (newProduct.getCount() != oldProduct.getCount()) {
            diffBundle.putLong(KEY_COUNT, newProduct.getCount());
        }
        if (diffBundle.size() == 0) {
            return null;
        }
        return diffBundle;
    }
}
