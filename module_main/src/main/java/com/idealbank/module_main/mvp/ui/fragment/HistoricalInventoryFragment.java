package com.idealbank.module_main.mvp.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R2.id.ll_mycollection_bottom_dialog)
    LinearLayout mLlMycollectionBottomDialog;
    @BindView(R2.id.btn_delete)
    Button mBtnDelete;
    @BindView(R2.id.select_all)
    TextView mSelectAll;
    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    private int mEditMode = MYLIVE_MODE_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;
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
        setRightText("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataEditMode();
            }
        });
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        getDate();
    }
    private void updataEditMode() {
        mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
        if (mEditMode == MYLIVE_MODE_EDIT) {
            setRightText("取消");
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            setRightText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
//            clearAll();
        }
        mAdapter.setEditMode(mEditMode);
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

    @OnClick({R2.id.fab, R2.id.btn_delete, R2.id.select_all})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.fab) {
            mRecyclerView.smoothScrollToPosition(0);
        } else if (i == R.id.btn_delete) {
            deleteVideo();
        } else if (i == R.id.select_all) {
            selectAllMain();
        }
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0) {
            mBtnDelete.setEnabled(false);
            return;
        }
        new AppDialog(_mActivity, DialogType.DEFAULT).setContent("删除后不可恢复，是否删除这" + index + "个条目？")
                .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                    @Override
                    public void onClick(String val) {
                    }
                })
                .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                    @Override
                    public void onClick(String val) {
                        for (int i = mAdapter.getData().size(), j = 0; i > j; i--) {
                            TaskBean myLive = mAdapter.getData().get(i - 1);
                            if (myLive.isSelect()) {
                                new DbManager().delTaskBeanWhereId(mAdapter.getData().get(i-1).getId());
//删除该任务id下的资产
                                new DbManager().delAssetsBeanWhereId(mAdapter.getData().get(i-1).getId());
                                mAdapter.getData().remove(myLive);
                                index--;
                            }
                        }
                        index = 0;
//                        mTvSelectNum.setText(String.valueOf(0));
                        setBtnBackground(index);
                        if (mAdapter.getData().size() == 0) {
                            mLlMycollectionBottomDialog.setVisibility(View.GONE);
                        }
                        mAdapter.notifyDataSetChanged();

                        getDate();
                    }
                })
                .show();
    }


    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (mAdapter == null) {
            return;
        }
        if (!isSelectAll) {
            for (int i = 0, j = mAdapter.getData().size(); i < j; i++) {
                mAdapter.getData().get(i).setSelect(true);
            }
            index = mAdapter.getData().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j = mAdapter.getData().size(); i < j; i++) {
                mAdapter.getData().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        mAdapter.notifyDataSetChanged();
        setBtnBackground(index);
    }
    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
            mBtnDelete.setTextColor(Color.WHITE);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnDelete.setEnabled(false);
            mBtnDelete.setTextColor(ContextCompat.getColor(_mActivity, R.color.color_b7b8bd));
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
