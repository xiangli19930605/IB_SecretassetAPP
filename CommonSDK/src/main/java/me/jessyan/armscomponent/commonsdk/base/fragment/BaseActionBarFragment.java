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
package me.jessyan.armscomponent.commonsdk.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.mvp.IPresenter;

import me.jessyan.armscomponent.commonsdk.R;
import me.jessyan.armscomponent.commonsdk.widget.ActionBar;

/**
 * ================================================
 * 有自定义ActionBar、无NormalView
 *
 * ================================================
 */
public abstract class BaseActionBarFragment<P extends IPresenter> extends BaseFragment<P> {

    ActionBar actionBar;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isActionBar()) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_base, null);
            ((ViewGroup) view.findViewById(R.id.fl_content)).addView(initView(inflater, container, savedInstanceState));
            actionBar= view.findViewById(R.id.actionbar);
        } else {
            view= initView(inflater, container, savedInstanceState);
        }
        return view;
    }

    protected void setTitleText(String title) {
//        LogUtils.warnInfo(title);
        if (actionBar != null) {
            actionBar.setCenterText(title);
        }

    }
    protected void setRightText(String title) {
        if (actionBar != null) {
            actionBar.setRightText(title);
        }

    }
    protected void setRightText(String title, View.OnClickListener leftlistener) {
        if (actionBar != null) {
            actionBar.setRightText(title,leftlistener);
        }

    }
    protected void setTitleText(String title, String righttitle, View.OnClickListener listener) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
            actionBar.setRightText( righttitle,listener);
        }
    }
    protected void setTitleText(String title ,View.OnClickListener listener) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
            actionBar.setLeftIcon(R.drawable.ic_back, listener);
        }
    }


    /**
     * 是否需要ActionBar
     * TODO 暂时用此方法 后续优化
     */
    @Override
    protected boolean isActionBar() {
        return true;
    }
    @Override
    protected boolean isNormalView() {
        return false;
    }



}
