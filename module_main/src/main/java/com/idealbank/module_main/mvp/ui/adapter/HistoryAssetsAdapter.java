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

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idealbank.module_main.R;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;

import java.util.List;

import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;


/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class HistoryAssetsAdapter extends BaseQuickAdapter<AssetsBean, BaseViewHolder> {
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架
    public HistoryAssetsAdapter(@Nullable List data) {
        super(R.layout.item_rv_history_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetsBean data) {


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

        ((SuperTextView) helper.getView(R.id.tv_id)).setCenterString(data.getRfidId());
        ((SuperTextView) helper.getView(R.id.stv_assetUser)).setCenterString(data.getAssetUser());
        ((SuperTextView) helper.getView(R.id.stv_belongDept)).setCenterString(data.getBelongDept());
        }


}