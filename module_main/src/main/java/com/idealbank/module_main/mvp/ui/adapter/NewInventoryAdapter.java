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

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idealbank.module_main.R;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import me.jessyan.armscomponent.commonres.widget.LoadingView;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.autosize.utils.LogUtils;


/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class NewInventoryAdapter extends BaseQuickAdapter<AssetsBean, NewInventoryAdapter.MyViewHolder> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;

    public NewInventoryAdapter(@Nullable List data) {
        super(R.layout.item_rv_swipemenu, data);
        countDownMap = new SparseArray<>();
    }


    @Override
    protected void convert(MyViewHolder helper, AssetsBean data) {
        AVLoadingIndicatorView avi = helper.getView(R.id.avi);
        if ((data.getPermissionState() == 4)) {
            boolean b = System.currentTimeMillis() - data.getTime() >= 5 * 1000;
            ((TextView) helper.getView(R.id.tv_state)).setText(b ? "重新查询" : "查询中");
            ((TextView) helper.getView(R.id.tv_state)).setClickable(b ? true : false);
            ((TextView) helper.getView(R.id.tv_state)).setEnabled(b ? true : false);
            helper.getView(R.id.tv_state).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TextView) helper.getView(R.id.tv_state)).setText("查询中");
                    ((TextView) helper.getView(R.id.tv_state)).setTextColor(Color.GREEN);
                    ((TextView) helper.getView(R.id.tv_state)).setClickable(false);
                    ((TextView) helper.getView(R.id.tv_state)).setEnabled( false);
                    avi.setVisibility(View.VISIBLE);
                    data.setSelect(false);
                    data.setTime(System.currentTimeMillis());
                    listener.click(helper.getLayoutPosition());
                }
            });
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(b ? Color.RED : Color.GREEN);
            ((ImageView) helper.getView(R.id.img_state)).setVisibility(View.GONE);
            avi.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        } else {
            avi.setVisibility(View.GONE);
            ((ImageView) helper.getView(R.id.img_state)).setVisibility(View.VISIBLE);
            ((TextView) helper.getView(R.id.tv_state)).setTextColor(Color.BLACK);
            if (data.getPermissionState() == 0) {
                ((TextView) helper.getView(R.id.tv_state)).setText("已授权");
                ((ImageView) helper.getView(R.id.img_state)).setImageResource(R.mipmap.ic_yes_big);
            } else if (data.getPermissionState() == 1) {
                ((TextView) helper.getView(R.id.tv_state)).setText("未授权");
                ((ImageView) helper.getView(R.id.img_state)).setImageResource(R.mipmap.ic_no_big);
            } else {
//                ((TextView) helper.getView(R.id.tv_state)).setText("临时授权");
                ((TextView) helper.getView(R.id.tv_state)).setText("查无此物");
                ((ImageView) helper.getView(R.id.img_state)).setImageResource(R.mipmap.ic_warn_big);
            }
        }
        ((SuperTextView) helper.getView(R.id.tv_id)).setCenterString(data.getRfidId());
        ((SuperTextView) helper.getView(R.id.stv_assetUser)).setCenterString(data.getAssetUser());
        ((SuperTextView) helper.getView(R.id.stv_belongDept)).setCenterString(data.getBelongDept());


        helper.addOnClickListener(R.id.content)
                .addOnClickListener(R.id.right)
                .addOnLongClickListener(R.id.ll_click);


    }


    public class MyViewHolder extends BaseViewHolder {
        private CountDownTimer countDownTimer;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    //整个布局的点击事件
    public interface onItemClickListener {
        void click(int position);
    }

    private onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }


}