package de.jonasrottmann.realmbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

import de.jonasrottmann.realmbrowser.model.RealmPreferences;
import de.jonasrottmann.realmbrowser.utils.Utils;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;

class RealmAdapter extends RecyclerView.Adapter<RealmAdapter.ViewHolder> {

    private final Context mContext;
    private final Listener mListener;
    private final RealmPreferences mRealmPreferences;
    private final DynamicRealm mRealm;
    private AbstractList<? extends DynamicRealmObject> mRealmObjects;
    private List<Field> mFieldList;



    public RealmAdapter(@NonNull Context context, @NonNull AbstractList<? extends DynamicRealmObject> realmObjects,
                        @NonNull List<Field> fieldList, @NonNull Listener listener, @NonNull DynamicRealm realm) {
        mRealmPreferences = new RealmPreferences(context);
        mContext = context;
        mRealmObjects = realmObjects;
        mFieldList = fieldList;
        mListener = listener;
        mRealm = realm;
    }



    public void setFieldList(List<Field> fieldList) {
        mFieldList = fieldList;
    }



    public void setRealmList(AbstractList<? extends DynamicRealmObject> realmObjects) {
        mRealmObjects = realmObjects;
    }



    @Override
    public RealmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.realm_browser_item_realm_browser, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public int getItemCount() {
        return mRealmObjects == null ? 0 : mRealmObjects.size();
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.realm_browser_grey));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.realm_browser_white));
        }

        if (mFieldList.isEmpty()) {
            holder.txtIndex.setText(null);
            holder.txtColumn1.setText(null);
            holder.txtColumn2.setText(null);
            holder.txtColumn3.setText(null);
        } else {
            holder.txtIndex.setText(String.valueOf(position));

            DynamicRealmObject realmObject = mRealmObjects.get(position);
            initRowWeight(holder);
            initRowTextWrapping(holder);
            initRowText(holder, realmObject);
        }
    }



    private void initRowWeight(ViewHolder holder) {
        LinearLayout.LayoutParams layoutParams2 = createLayoutParams();
        LinearLayout.LayoutParams layoutParams3 = createLayoutParams();

        if (mFieldList.size() == 1) {
            layoutParams2.weight = 0;
            layoutParams3.weight = 0;
        } else if (mFieldList.size() == 2) {
            layoutParams2.weight = 1;
            layoutParams3.weight = 0;
        } else if (mFieldList.size() == 3) {
            layoutParams2.weight = 1;
            layoutParams3.weight = 1;
        }
        holder.txtColumn2.setLayoutParams(layoutParams2);
        holder.txtColumn3.setLayoutParams(layoutParams3);
    }



    private void initRowTextWrapping(ViewHolder holder) {
        boolean shouldWrapText = mRealmPreferences.shouldWrapText();
        holder.txtColumn1.setSingleLine(!shouldWrapText);
        holder.txtColumn2.setSingleLine(!shouldWrapText);
        holder.txtColumn3.setSingleLine(!shouldWrapText);
    }



    private void initRowText(ViewHolder holder, DynamicRealmObject realmObject) {
        if (mFieldList.size() == 1) {
            initFieldText(holder.txtColumn1, realmObject, mFieldList.get(0));
            holder.txtColumn2.setText(null);
            holder.txtColumn3.setText(null);
        } else if (mFieldList.size() == 2) {
            initFieldText(holder.txtColumn1, realmObject, mFieldList.get(0));
            initFieldText(holder.txtColumn2, realmObject, mFieldList.get(1));
            holder.txtColumn3.setText(null);
        } else if (mFieldList.size() == 3) {
            initFieldText(holder.txtColumn1, realmObject, mFieldList.get(0));
            initFieldText(holder.txtColumn2, realmObject, mFieldList.get(1));
            initFieldText(holder.txtColumn3, realmObject, mFieldList.get(2));
        }
    }



    private void initFieldText(TextView txtColumn, DynamicRealmObject realmObject, Field field) {
        txtColumn.setText(Utils.getFieldValueString(realmObject, field));
        txtColumn.setOnClickListener(createClickListener(realmObject));
    }



    private View.OnClickListener createClickListener(@NonNull final DynamicRealmObject realmObject) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRowClicked(realmObject);
            }
        };
    }



    private LinearLayout.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    public interface Listener {
        void onRowClicked(@NonNull DynamicRealmObject realmObject);
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtIndex;
        public final TextView txtColumn1;
        public final TextView txtColumn2;
        public final TextView txtColumn3;



        public ViewHolder(View v) {
            super(v);
            txtIndex = (TextView) v.findViewById(R.id.realm_browser_txtIndex);
            txtColumn1 = (TextView) v.findViewById(R.id.realm_browser_txtColumn1);
            txtColumn2 = (TextView) v.findViewById(R.id.realm_browser_txtColumn2);
            txtColumn3 = (TextView) v.findViewById(R.id.realm_browser_txtColumn3);
        }
    }
}