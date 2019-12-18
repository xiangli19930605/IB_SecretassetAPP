/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.idealbank.module_main.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idealbank.module_main.R;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;


/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class HistoryInventoryAdapter extends BaseQuickAdapter<TaskBean, BaseViewHolder> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架
    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;
    public HistoryInventoryAdapter(@Nullable List data) {
        super(R.layout.item_rv_historyinventory, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskBean data) {

        Drawable titleDrawable;

        String flag;
        if (data.getPassFlag() == 0) {
            titleDrawable = mContext.getResources().getDrawable(R.mipmap.ic_yes);
            flag = "允许通行";
        } else {
            flag = "拒绝通行";
            titleDrawable = mContext.getResources().getDrawable(R.mipmap.ic_no);
        }

        ((SuperTextView) helper.getView(R.id.tv_result)).setCenterTvDrawableRight(titleDrawable).setCenterString(flag + "(" + data.getReason() + ")");


        ((SuperTextView) helper.getView(R.id.stv_createTime)).setCenterString(data.getCreateTime());
        ((SuperTextView) helper.getView(R.id.stv_num)).setCenterString("" + data.getNumber());
        ((SuperTextView) helper.getView(R.id.stv_taskid)).setCenterString(data.getTaskid());
        if (mEditMode == MYLIVE_MODE_CHECK) {
            ((ImageView) helper.getView(R.id.check_box)).setVisibility(View.GONE);
        } else {
            ((ImageView) helper.getView(R.id.check_box)).setVisibility(View.VISIBLE);
            if (data.isSelect()) {
                ((ImageView) helper.getView(R.id.check_box)).setImageResource(R.mipmap.ic_checked);
            } else {
                ((ImageView) helper.getView(R.id.check_box)).setImageResource(R.mipmap.ic_uncheck);
            }
        }
        helper.addOnClickListener(R.id.content).addOnClickListener(R.id.right_menu_1);
    }

    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }
}