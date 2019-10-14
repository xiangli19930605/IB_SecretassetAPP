package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.mvp.ui.adapter.HistoryAssetsAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerHistoricalInventoryDetailsComponent;
import com.idealbank.module_main.mvp.contract.HistoricalInventoryDetailsContract;
import com.idealbank.module_main.mvp.presenter.HistoricalInventoryDetailsPresenter;

import com.idealbank.module_main.R;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;



/**
 * ================================================
 * Description:历史盘查详情
 * <p>
 * ================================================
 */
public class HistoricalInventoryDetailsFragment extends BaseActionBarFragment<HistoricalInventoryDetailsPresenter> implements HistoricalInventoryDetailsContract.View {

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.stv_taskid)
    SuperTextView stv_taskid;
    @BindView(R2.id.stv_createTime)
    SuperTextView stv_createTime;
    @BindView(R2.id.stv_num)
    SuperTextView stv_num;
    @BindView(R2.id.tv_result)
    SuperTextView tv_result;
    @Inject
    HistoryAssetsAdapter mAdapter;
    @Inject

    List<AssetsBean> mList;
    TaskBean taskBean;

    public static HistoricalInventoryDetailsFragment newInstance(TaskBean taskBean) {
        HistoricalInventoryDetailsFragment fragment = new HistoricalInventoryDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskBean", (Serializable) taskBean);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHistoricalInventoryDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historical_inventory_details, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        taskBean = (TaskBean) getArguments().getSerializable("taskBean");
        getDate();
        setTitleText("历史详情");
        Drawable titleDrawable;
        String flag;
        if (taskBean.getPassFlag() == 0) {
            titleDrawable = getResources().getDrawable(R.mipmap.ic_yes);
            flag = "允许通行";
        } else {
            flag = "拒绝通行";
            titleDrawable = getResources().getDrawable(R.mipmap.ic_no);
        }
        tv_result.setCenterTvDrawableRight(titleDrawable).setCenterString(flag + "(" + taskBean.getReason() + ")");

        stv_taskid.setCenterString(taskBean.getTaskid());
        stv_createTime.setCenterString(taskBean.getCreateTime());
        stv_num.setCenterString(""+taskBean.getNumber());
    }

    private void getDate() {
        mList = new DbManager().queryAssetsBeanWhereTaskid(taskBean.getTaskid());
        mAdapter.replaceData(mList);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                start(AssetsDetailsFragment.newInstance());
//                ((MainFragment) getParentFragment()).startBrotherFragment(AssetsDetailsFragment.newInstance());
                start(AssetsDetailsFragment.newInstance(mList.get(position)));
            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    protected void initEventAndData() {

    }


}
