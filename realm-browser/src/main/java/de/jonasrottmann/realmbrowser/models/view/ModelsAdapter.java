package de.jonasrottmann.realmbrowser.models.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.jonasrottmann.realmbrowser.models.model.ModelPojo;
import java.util.ArrayList;
import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ModelsAdapter extends RecyclerView.Adapter<ModelsAdapter.ViewHolder> {

    private final List<ModelPojo> files;
    private final OnModelSelectedListener listener;

    ModelsAdapter(@NonNull ArrayList<ModelPojo> list, @NonNull OnModelSelectedListener listener) {
        this.files = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelPojo model = files.get(position);
        holder.title.setText(model.getKlass().getSimpleName());
        holder.subTitle.setText(String.valueOf(model.getCount()));
        holder.itemView.setOnClickListener(createClickListener(this.files.get(position)));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals(ModelsDiffUtilsCallback.KEY_CLASS)) {
                    holder.title.setText(((Class) o.getSerializable(key)).getSimpleName());
                } else if (key.equals(ModelsDiffUtilsCallback.KEY_COUNT)) {
                    holder.subTitle.setText(o.getString(key));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.files.size();
    }

    void swapList(ArrayList<ModelPojo> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ModelsDiffUtilsCallback(this.files, newList));
        diffResult.dispatchUpdatesTo(this);

        this.files.clear();
        this.files.addAll(newList);
    }

    private View.OnClickListener createClickListener(@NonNull final ModelPojo model) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onModelSelected(model);
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subTitle;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(android.R.id.text1);
            subTitle = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    interface OnModelSelectedListener {
        void onModelSelected(ModelPojo file);
    }
}