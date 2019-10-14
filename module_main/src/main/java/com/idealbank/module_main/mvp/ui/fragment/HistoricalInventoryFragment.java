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
import com.idealbank.module_main.Netty.InstructUtils;
import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.utils.UsbUtils;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.idealbank.module_main.mvp.ui.adapter.HistoryInventoryAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerHistoricalInventoryComponent;
import com.idealbank.module_main.mvp.contract.HistoricalInventoryContract;
import com.idealbank.module_main.mvp.presenter.HistoricalInventoryPresenter;

import com.idealbank.module_main.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.simple.eventbus.Subscriber;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseRootFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:历史盘查任务
 * <p>
 * ================================================
 */
public class HistoricalInventoryFragment extends BaseRootFragment<HistoricalInventoryPresenter> implements HistoricalInventoryContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.normal_view)
    SmartRefreshLayout refreshLayout;
    @Inject
    HistoryInventoryAdapter mAdapter;
    @Inject
    List<TaskBean> mList;

    public static HistoricalInventoryFragment newInstance() {
        HistoricalInventoryFragment fragment = new HistoricalInventoryFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHistoricalInventoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_historical_inventory, container, false);
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "initData");
        setTitleText("历史记录");
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        getDate();
    }

    @Override
    protected void reload() {

    }


    private void getDate() {

//        if (UsbUtils.getUsbType()) {
//            Message message = new Message();
//            message.setId(1);
//            message.setType(MsgType.HISTORY);
//            if (InstructUtils.send(message)) {
//            } else {
//                mAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null));
//            }
//        } else {}
            mList = new DbManager().queryTaskBeanWhereState(1);
            Collections.reverse(mList);
            mAdapter.replaceData(mList);
            if (mList.size() == 0) {
                mAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null));
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
                    ((MainFragment) getParentFragment()).startBrotherFragment(HistoricalInventoryDetailsFragment.newInstance(mList.get(position)));
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

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    protected void initEventAndData() {

        Log.e(TAG, "initEventAndData");

    }

    @OnClick({R2.id.fab})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.fab) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Subscriber(tag = EventBusTags.REFRESH_HIS)
    private void updateUser(Event event) {
        getDate();
    }

    //接受终端返回的数据
    @Subscriber(tag = EventBusTags.HISTORY)
    private void getHistory(Event event) {
        //位于栈顶才接收
        Log.e("SEARCH_RFID:", "SEARCH_RFID");

    }
}
