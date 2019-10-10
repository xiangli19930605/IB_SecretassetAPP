package com.idealbank.module_main.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.mvp.ui.adapter.CurrentInventoryAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerCurrentInventoryComponent;
import com.idealbank.module_main.mvp.contract.CurrentInventoryContract;
import com.idealbank.module_main.mvp.presenter.CurrentInventoryPresenter;

import com.idealbank.module_main.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.simple.eventbus.Subscriber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseRootFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:当前盘查任务
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 14:55
 * ================================================
 */
public class CurrentInventoryFragment extends BaseRootFragment<CurrentInventoryPresenter> implements CurrentInventoryContract.View {

    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.normal_view)
    SmartRefreshLayout refreshLayout;
    @Inject
    CurrentInventoryAdapter mAdapter;
    @Inject
    List<TaskBean> mList;


    public static CurrentInventoryFragment newInstance() {
        CurrentInventoryFragment fragment = new CurrentInventoryFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCurrentInventoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_inventory, container, false);
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "initData");
        setTitleText("当前盘查");
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
//        showLoad();
//        showError();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    protected void initEventAndData() {
        Log.e(TAG, "initEventAndData");
//        getDate();
//        showLoad();
//        showError();
        getDate();
    }

    @Override
    protected void reload() {
        Log.e(TAG, "reload");
        showNormal();
    }

    private void getDate() {
        mList = new DbManager().queryTaskBeanWhereState(0);
        Collections.reverse(mList);
        mAdapter.replaceData(mList);
        if(mList.size()==0){
            mAdapter.setEmptyView( LayoutInflater.from(getContext()).inflate(R.layout.empty_layout,null));
        }
        refreshLayout.finishRefresh();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDate();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.content) {
                    ((MainFragment) getParentFragment()).startBrotherFragment(CurrentInventoryDetailsFragment.newInstance(mList.get(position)));
                } else if (view.getId() == R.id.right_menu_1) {
                    new AppDialog(_mActivity, DialogType.DEFAULT).setTitle("确定从盘查表中删除此项？")
                            .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                                @Override
                                public void onClick(String val) {
                                }
                            })
                            .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                                @Override
                                public void onClick(String val) {
                                    new DbManager().delTaskBeanWhereId(mList.get(position).getId());
                                    //删除该任务id下的资产
                                    new DbManager().delAssetsBeanWhereTaskId(mList.get(position).getTaskid());
                                    ToastUtil.showToast("删除成功");
                                    getDate();
                                }
                            })
                            .show();
                }
            }
        });


    }


    @Subscriber(tag = EventBusTags.REFRESH_CUR)
    private void updateUser(Event event) {
        getDate();
    }

}
