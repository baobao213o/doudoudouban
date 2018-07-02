package com.xxx.syy.ui.search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxx.syy.R;
import com.xxx.syy.entity.History;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private ArrayList<History> mDatas;
    private OnItemClickListener mOnItemClickListener;

    SearchHistoryAdapter(Activity context, ArrayList<History> datas) {
        super();
        this.mContext = context;
        this.mDatas = datas;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setList(ArrayList<History> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public ArrayList<History> getList() {
        return mDatas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.syy_item_search_history, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        History history = mDatas.get(position);
        ((HistoryHolder) holder).tv_syy_search_item_q.setText(history.q);
        ((HistoryHolder) holder).tv_syy_search_item_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView tv_syy_search_item_q;

        HistoryHolder(View view) {
            super(view);
            tv_syy_search_item_q = view.findViewById(R.id.tv_syy_search_item_q);
        }
    }

}