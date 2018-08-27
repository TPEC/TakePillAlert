package pers.tpec.takepills;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewProvider;

public class TPInfoProvider extends ItemViewProvider<TPInfo, TPInfoProvider.ViewHolder> {
    private OnItemClickListener onItemClickListener;

    public TPInfoProvider(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View rootView = inflater.inflate(R.layout.adaptor_listitem, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TPInfo tpInfo) {
        holder.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClickListener.longClick(view, tpInfo);
                return true;
            }
        });
        TextView dateTv = holder.getRootView().findViewById(R.id.date_tv);
        TextView amountTv = holder.getRootView().findViewById(R.id.amount_tv);
        TextView tagTv = holder.getRootView().findViewById(R.id.tag_tv);
        dateTv.setText(tpInfo.getDateString());
        amountTv.setText(tpInfo.getAmountString());
        tagTv.setText(tpInfo.getTagString());
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
