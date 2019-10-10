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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.Netty.ChannelMap;
import com.idealbank.module_main.Netty.InstructUtils;
import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.R;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.rfid_module.ONDEVMESSAGE;
import com.idealbank.module_main.app.rfid_module.RfidData;
import com.idealbank.module_main.app.utils.RfidDataUtils;
import com.idealbank.module_main.app.utils.UsbUtils;
import com.idealbank.module_main.di.component.DaggerCurrentInventoryDetailsComponent;
import com.idealbank.module_main.mvp.contract.CurrentInventoryDetailsContract;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.idealbank.module_main.mvp.presenter.CurrentInventoryDetailsPresenter;
import com.idealbank.module_main.mvp.ui.adapter.NewInventoryAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.LogUtils;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static me.jessyan.armscomponent.commonsdk.utils.ToastUtil.showToast;


/**
 * ================================================
 * Description:当前盘查事件详情
 *
 * <p>
 * ================================================
 */
public class CurrentInventoryDetailsFragment extends BaseActionBarFragment<CurrentInventoryDetailsPresenter> implements CurrentInventoryDetailsContract.View {
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @Inject
    NewInventoryAdapter mAdapter;
    @Inject
    List<AssetsBean> mList;
    TaskBean taskBean;
    @BindView(R2.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R2.id.btn_finish)
    Button btn_finish;
    @BindView(R2.id.tv_num)
    TextView tv_num;
    @BindView(R2.id.tv_time)
    TextView tv_time;
    Boolean isopen = false;

    public ONDEVMESSAGE OnMsg = new ONDEVMESSAGE();
    public static CurrentInventoryDetailsFragment newInstance(TaskBean taskBean) {
        CurrentInventoryDetailsFragment fragment = new CurrentInventoryDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskBean", (Serializable) taskBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCurrentInventoryDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_inventory_details, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        setTitleText("任务详情");
        setRightText("开始盘点", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isopen) {
                    setRightText("开始盘点");
                    MyApplication.sv_Main.DoInventoryTag(false);
                    isopen = false;
                } else {
                    setRightText("停止盘点");
                    MyApplication.sv_Main.DoInventoryTag(true);
                    isopen = true;
                }
            }
        });

        taskBean = (TaskBean) getArguments().getSerializable("taskBean");
        initRecyclerView();
        tv_time.setText(taskBean.getCreateTime());
        mRecyclerView.setAdapter(mAdapter);
        getDate();

        //开启服务
        boolean b = MyApplication.sv_Main.Create(OnMsg);
        //先关闭盘点
        MyApplication.sv_Main.DoInventoryTag(false);
        // rfid power on
        MyApplication. sv_Main.gpio_rfid_config(true);
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return aLong + 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addDispose(d);
                    }
                    @Override
                    public void onNext(Long count) {
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            boolean b = System.currentTimeMillis() - mAdapter.getData().get(i).getTime() >= 5 * 1000;
                            if (b&&mAdapter.getData().get(i).getSelect()==false) {
                                mAdapter.getData().get(i).setSelect(true);
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getDate() {
        mList = new DbManager().queryAssetsBeanWhereTaskid(taskBean.getTaskid());
        mAdapter.replaceData(mList);
        //更新数量 number>0更新，否则删除 放在数据库处理了
        new DbManager().upDateNumberTaskBeanWhereId(taskBean.getTaskid(), mList.size());
        tv_num.setText("共" + mList.size() + "件物品");
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view.getId() == R.id.right) {
                    new AppDialog(_mActivity, DialogType.DEFAULT).setTitle("确定从盘查表中删除此项？")
                            .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                                @Override
                                public void onClick(String val) {
                                }
                            })
                            .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                                @Override
                                public void onClick(String val) {
                                    LogUtils.debugInfo("" + mList.size());
                                    new DbManager().delAssetsBeanWhereId(mList.get(position).getUid());
                                    getDate();

                                }

                            })
                            .show();
                } else if (view.getId() == R.id.content) {
                    start(AssetsDetailsFragment.newInstance(mList.get(position)));
                }

            }
        });
        mAdapter.setListener(new NewInventoryAdapter.onItemClickListener() {
            @Override
            public void click(int position) {
//判断是否离线，离线访问离线数据库，不是离线，访问请求
                List<AssetsBean> addData = new ArrayList<>();
                AssetsBean assetsBean = mList.get(position);
                addData.add(assetsBean);
                if (UsbUtils.getUsbType()) {
                    InstructUtils.send( mList.get(position),taskBean.getCreateTime());
                } else {
                    RfidDataUtils.changeOffline(_mActivity,addData,taskBean.getTaskid());
                }
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

    @OnClick({R2.id.btn_finish,R2.id.btn_refuse, R2.id.btn_allow})
    void onClick(View view) {
        int i = view.getId();
         if (i == R.id.btn_finish) {
            ll_bottom.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);

            if (ChannelMap.getChannel("1") != null) {
                Message message = new Message();
                message.setId(1);
                message.setType(MsgType.UPLOADDATA);
                UpLoad upLoad = new UpLoad();
                upLoad.setCreateTime(taskBean.getCreateTime());
                upLoad.setAssetList(mList);
                message.setResponseMessage(upLoad);
                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
            } else {
                showToast("未连接");
            }
        }  else if (i == R.id.btn_refuse) {
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择拒绝通行理由").setItemType(AppDialog.REFUSE).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {
                @Override
                public void onClick(String val) {
                    Message message = new Message();
                    message.setId(1);
                    message.setType(MsgType.UPLOADDATA);
                    UpLoad upLoad = new UpLoad();
                    upLoad.setCreateTime(taskBean.getCreateTime());
                    upLoad.setReason(val);
                    upLoad.setPassFlag(1);
                    upLoad.setAssetList(mList);
                    message.setResponseMessage(upLoad);
                    InstructUtils.send(message);

                }
            }).show();
        }  else if (i == R.id.btn_allow) {
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择允许通行理由").setItemType(AppDialog.ALLOW).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {
                @Override
                public void onClick(String val) {
                    Message message = new Message();
                    message.setId(1);
                    message.setType(MsgType.UPLOADDATA);
                    UpLoad upLoad = new UpLoad();
                    upLoad.setCreateTime(taskBean.getCreateTime());
                    upLoad.setReason(val);
                    upLoad.setPassFlag(0);
                    upLoad.setAssetList(mList);
                    message.setResponseMessage(upLoad);
                    InstructUtils.send(message);
                }
            }).show();
        }
    }



    //接受终端返回的数据
    @Subscriber(tag = EventBusTags.UPLOADDATA)
    private void upload(Event event) {
        //位于栈顶才接收
        if (getTopFragment() instanceof CurrentInventoryDetailsFragment) {
            if (event.getAction() == "1") {
                ToastUtil.showToast(ToastUtil.TPYE_FAILURE, "上传成功");
                //上传修改数据库任务状态
                new DbManager().upDateTaskBeanWhereId(taskBean.getId(), 1);
                EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_CUR);
                EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_HIS);
                pop();
            } else {
                ToastUtil.showToast(ToastUtil.TPYE_FAILURE, "上传失败");
            }
        }
    }
    //接受手持机扫描
    @Subscriber(tag = EventBusTags.SCANRFID)
    private void scaanRfid(Event event) {
        //位于栈顶才接收
        Log.e("SCANRFID:", "SCANRFID");
        if (getTopFragment() instanceof CurrentInventoryDetailsFragment) {
            List<AssetsBean> addData = new ArrayList<>();
            RfidData rfidData = (RfidData) event.getData();
            AssetsBean assetsBean = new AssetsBean(null, null, rfidData.getTagid(), taskBean.getTaskid(), rfidData.getTagType(), "", "", "", "", "", "", DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL), "", "", "", "", 4, 0);
            assetsBean.setTime(System.currentTimeMillis());
            if (new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskBean.getTaskid(), rfidData.getTagid()) != null) {
                return;
            }
            addData.add(assetsBean);
            //再插入数据
            new DbManager().insertAssetsBean(assetsBean);
            getDate();
            //判断是否离线，离线访问离线数据库，不是离线，访问请求
            if (UsbUtils.getUsbType()) {
                Message message = new Message();
                message.setId(1);
                message.setType(MsgType.RFID);
                UpLoad upLoad = new UpLoad();
                upLoad.setId("1");
                upLoad.setDeviceId("");
                upLoad.setCreateTime(taskBean.getCreateTime());
                upLoad.setAssetList(addData);
                message.setResponseMessage(upLoad);
                InstructUtils.send(message);
            } else {
                RfidDataUtils.changeOffline(_mActivity,addData,taskBean.getTaskid());
                getDate();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            MyApplication.sv_Main.DoInventoryTag(false);
            MyApplication.sv_Main.DoIndentify(false);
            MyApplication.sv_Main.gpio_rfid_config(false);
            MyApplication.sv_Main.Free();
        } catch (UnsupportedOperationException e) {
            System.out.print("******************");
        } catch (Exception e) {
            System.out.print("******************");
        }
        EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_CUR);

    }


}
