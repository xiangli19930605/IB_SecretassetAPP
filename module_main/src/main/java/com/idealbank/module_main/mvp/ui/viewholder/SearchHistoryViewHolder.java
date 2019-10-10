package com.idealbank.module_main.mvp.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.ButterKnife;

/**
 * @author quchao
 * @date 2018/3/23
 */

public class SearchHistoryViewHolder extends BaseViewHolder {

//    @BindView(R.id.item_search_history_tv)
//    TextView mSearchHistoryTv;

    public SearchHistoryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
