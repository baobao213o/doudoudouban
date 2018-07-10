package com.xxx.syy.ui.search;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxx.syy.R;
import com.xxx.syy.entity.History;

import java.util.ArrayList;

public class SearchHistoryAdapter extends BaseQuickAdapter<History, BaseViewHolder> {


    SearchHistoryAdapter(ArrayList<History> datas) {
        super(R.layout.syy_item_search_history, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, History item) {
        ((TextView) holder.getView(R.id.tv_syy_search_item_q)).setText(item.q);
    }

}