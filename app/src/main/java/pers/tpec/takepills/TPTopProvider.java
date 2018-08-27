package pers.tpec.takepills;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;

public class TPTopProvider extends ItemViewProvider<String, TPTopProvider.ViewHolder> {
    private OnItemClickListener onItemClickListener;

    public TPTopProvider(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View rootView = inflater.inflate(R.layout.adaptor_listitem_top, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final String str) {
        TextView dateTv = holder.getRootView().findViewById(R.id.date_tv);
        dateTv.setText(str);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }

        View getRootView() {
            return itemView;
        }
    }
}
