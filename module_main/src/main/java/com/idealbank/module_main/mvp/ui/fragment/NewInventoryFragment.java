package com.idealbank.module_main.mvp.ui.fragment;

import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.Netty.ChannelMap;
import com.idealbank.module_main.Netty.InstructUtils;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.R2;

import butterknife.OnCheckedChanged;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.CommonUtils;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.rfid_module.ONDEVMESSAGE;
import com.idealbank.module_main.app.rfid_module.RfidData;
import com.idealbank.module_main.app.service.NetWorkReceiver;
import com.idealbank.module_main.app.utils.RfidDataUtils;
import com.idealbank.module_main.app.utils.UsbUtils;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.bean.UpLoadAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.idealbank.module_main.mvp.ui.adapter.NewInventoryAdapter;
import com.idealbank.module_main.mvp.ui.adapter.NewInventoryAdapter1;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerNewInventoryComponent;
import com.idealbank.module_main.mvp.contract.NewInventoryContract;
import com.idealbank.module_main.mvp.presenter.NewInventoryPresenter;

import com.idealbank.module_main.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.simple.eventbus.Subscriber;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.autosize.utils.LogUtils;
import me.yokeyword.fragmentation.SupportActivity;

import static android.content.Context.VIBRATOR_SERVICE;
import static me.jessyan.armscomponent.commonsdk.utils.ToastUtil.showToast;


/**
 * ================================================
 * Description:
 * <p>
 * ================================================
 */
public class NewInventoryFragment extends BaseActionBarFragment<NewInventoryPresenter> implements NewInventoryContract.View {
    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R2.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R2.id.tv_time)
    TextView tv_time;
    @BindView(R2.id.tv_num)
    TextView tv_num;
    @BindView(R2.id.btn_finish)
    Button btn_finish;
    @BindView(R2.id.btn_switch)
    CheckBox btn_switch;
    @Inject
    NewInventoryAdapter mAdapter;

    @Inject
    List<AssetsBean> mList;
    TaskBean taskBean;
    String taskid;
    String reason;
    int passFlag;

    String createTime;
    Boolean isopen = false;
    public ONDEVMESSAGE OnMsg = new ONDEVMESSAGE();

    public static NewInventoryFragment newInstance() {
        NewInventoryFragment fragment = new NewInventoryFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerNewInventoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_inventory, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitleText("新增盘查", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsDialog();
            }
        });
        setRightText("清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppDialog(_mActivity, DialogType.DEFAULT).setTitle("确定清空全部资产？")
                        .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                            }
                        })
                        .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                                new DbManager().delAssetsBeanWhereTaskId(taskid);
                                getDate();
                                showToast("清空成功");
                            }
                        })
                        .show();

            }
        });
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        mList = mAdapter.getData();

        taskid = DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_ALL);
        createTime = DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL);
        tv_time.setText(createTime);
        //增加任务
        taskBean = new TaskBean();
        taskBean.setState(0);
        taskBean.setTaskid(taskid);
        taskBean.setNumber(mAdapter.getData().size());
        taskBean.setCreateTime(createTime);
        taskBean.setFinishTime(DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL));
        new DbManager().insertTaskBean(taskBean);



        time = new TimeCount(10000, 1000);
    }

    int a = 0;

    //    R2.id.btn_add, R2.id.btn_edit, R2.id.btn_query,
    @OnClick({R2.id.btn_finish, R2.id.btn_refuse, R2.id.btn_allow, R2.id.btn_switch})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_finish) {
            ll_bottom.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);
        } else if (id == R.id.btn_refuse) {
            if (mAdapter.getData().size() == 0) {
                ArmsUtils.snackbarText("请添加资产再提交");
                return;
            }
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择拒绝通行理由").setItemType(AppDialog.REFUSE).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {
                @Override
                public void onClick(String val) {
                    passFlag = 1;
                    reason = val;
                    if (Constants.ISNETORSOCKET) {
                        mPresenter.saveCheckTask(InstructUtils.getUpLoadAssetsBean(taskid, createTime, val, mAdapter.getData()));
                    } else {
                        InstructUtils.send(InstructUtils.getUPLOADDATAMessage(createTime, val, mAdapter.getData()));
                    }
                }
            }).show();
        } else if (id == R.id.btn_allow) {
            if (mAdapter.getData().size() == 0) {
                ArmsUtils.snackbarText("请添加资产再提交");
                return;
            }
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择允许通行理由").setItemType(AppDialog.ALLOW).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {
                @Override
                public void onClick(String val) {
                    passFlag = 0;
                    reason = val;
                    if (Constants.ISNETORSOCKET) {
                        mPresenter.saveCheckTask(InstructUtils.getUpLoadAssetsBean(taskid, createTime, val, mAdapter.getData()));
                    } else {
                        InstructUtils.send(InstructUtils.getUPLOADDATAMessage(createTime, val, mAdapter.getData()));
                    }
                }
            }).show();
        }
//        else if (id == R.id.btn_edit) {
//            new AppDialog(getContext(), DialogType.INPUT)
//                    .setTitle("修改项")
//                    .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
//                        @Override
//                        public void onClick(String val) {
//                        }
//                    })
//                    .setRightButton("确定", new AppDialog.OnButtonClickListener() {
//                        @Override
//                        public void onClick(String val) {
//                            if (val != null && !val.equals("")) {
//                                AssetsBean assets = mList.get(Integer.valueOf(val));
//                                assets.setPermissionState(1);
//                                mAdapter.setData(Integer.valueOf(val), assets);
//                            }
//                        }
//                    })
//                    .show();
//        }
//        else if (id == R.id.btn_add) {
//            List<AssetsBean> addData = new ArrayList<>();
//            AssetsBean assetsBean = new AssetsBean(null, null, "100" + (a + 0), taskid, 0, "", "", "", "", "", "", "", "", "", "", "", 4, 0);
//            assetsBean.setTime(System.currentTimeMillis());
//            addData.add(assetsBean);
//            a = a + 1;
//            //再插入数据
//            new DbManager().insertInTxAssetsBean(addData);
//            getDate();
//            //判断是否离线，离线访问离线数据库，不是离线，访问请求
////            if (UsbUtils.getUsbType()) {
////                InstructUtils.send(InstructUtils.getRFIDMessage(addData));
////
////            } else {
////                RfidDataUtils.changeOffline(_mActivity, addData, taskid);
////                getDate();
////            }
//
//            if (CommonUtils.isNetworkConnected()) {
//                mPresenter.getListByRfid(InstructUtils.getUpAssetsBean(addData));
//            } else {
//                RfidDataUtils.changeOffline(addData, taskid);
//            }
//
//
//        } else if (id == R.id.btn_query) {
//            //模拟离线查询
//            if (UsbUtils.getUsbType()) {
//                List<AssetsBean> addData = new DbManager().queryAssetsBeanWhereTaskid(taskid);
//                for (int i = 0; i < addData.size(); i++) {
//                    AssetsBean assetsBean = addData.get(i);
//                    if (i == 0) {
//                        assetsBean.setPermissionState(0);
//                        assetsBean.setBelongDept("技术");
//                    } else if (i == 1) {
//                        assetsBean.setPermissionState(1);
//                        assetsBean.setBelongDept("香炉");
//                    } else if (i == 2) {
//                        assetsBean.setPermissionState(3);
//                        assetsBean.setBelongDept("智能");
//                    }
//                }
//                EventBusUtils.sendEvent(new Event(addData), EventBusTags.SEARCH_RFID);
//            } else {
//                InstructUtils.send(InstructUtils.getRFIDMessage(mList));
//            }
//        }


    }

    @OnCheckedChanged({R2.id.btn_switch})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.btn_switch) {
            Log.e("isChecked", "" + isChecked);
            if (isChecked) {
//                MyApplication.sv_Main.DoInventoryTag(true);
                openRfid();
                btn_switch.setText("停止盘点");
                time.start();
            } else {
                btn_switch.setText("开始盘点");
                closeRfid();
//                MyApplication.sv_Main.DoInventoryTag(false);
                time.onFinish();
            }
        }
    }

    private void getDate() {
        mList = new DbManager().queryAssetsBeanWhereTaskid(taskid);
        mAdapter.replaceData(mList);
        tv_num.setText("共" + mAdapter.getData().size() + "件物品");
    }


    @Override
    protected void initEventAndData() {

    }


    public void ChangeData(List<AssetsBean> list) {
//查询数据库当前taskid 下的资产
        for (int i = 0; i < list.size(); i++) {
            //遍历返回的数据，查询数据库中是否含有改数据有则修改
            AssetsBean assetsBean = new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskid, list.get(i).getRfidId());
            assetsBean.setAssetUser(list.get(i).getAssetUser());
            assetsBean.setAssetIncId(list.get(i).getAssetIncId());
            assetsBean.setBelongDept(list.get(i).getBelongDept());
            assetsBean.setLastApproveUser(list.get(i).getLastApproveUser());
            assetsBean.setId(list.get(i).getId());
            assetsBean.setOutBillId(list.get(i).getOutBillId());
            assetsBean.setAssetId(list.get(i).getAssetId());
            assetsBean.setPermissionState(list.get(i).getPermissionState());
            assetsBean.setAssetState(list.get(i).getAssetState());
            assetsBean.setEndTime(list.get(i).getEndTime());
            assetsBean.setAssetName(list.get(i).getAssetName());
            assetsBean.setTypeId(list.get(i).getTypeId());
            assetsBean.setAssetBrand(list.get(i).getAssetBrand());
            assetsBean.setAssetModel(list.get(i).getAssetModel());
            new DbManager().upAssetsBeanWhereId(assetsBean);
        }
        getDate();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(true);//列表翻转
        ArmsUtils.configRecyclerView(mRecyclerView, layout);
        mRecyclerView.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
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
                                    new DbManager().delAssetsBeanWhereId(mAdapter.getData().get(position).getUid());
                                    showToast("删除成功");
                                    getDate();
                                }
                            })
                            .show();
                } else if (view.getId() == R.id.content) {
                    start(AssetsDetailsFragment.newInstance(mAdapter.getData().get(position)));
                } else if (view.getId() == R.id.tv_state) {
                    List<AssetsBean> addData = new ArrayList<>();
                    AssetsBean assetsBean = mAdapter.getData().get(position);
                    addData.add(assetsBean);
                    if (Constants.ISNETORSOCKET) {
                        if (CommonUtils.isNetworkConnected()) {
                            mPresenter.getListByRfid(InstructUtils.getUpAssetsBean(addData));
                        } else {
                            RfidDataUtils.changeOffline(addData, taskid);
                            getDate();
                        }
                    } else {
                        //判断是否离线，离线访问离线数据库，不是离线，访问请求
                        if (UsbUtils.getUsbType()) {
                            InstructUtils.send(InstructUtils.getRFIDMessage(addData));
                        } else {
                            RfidDataUtils.changeOffline(_mActivity, addData, taskid);
                            getDate();
                        }
                    }
                }
            }
        });

        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                start(TagFragment.newInstance(mList.get(position).getRfidId()));
                return true;
            }
        });

        //处理10秒查询机制
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
                            if (b && mAdapter.getData().get(i).getSelect() == false) {
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

    private TimeCount time;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (btn_switch != null && btn_switch.isChecked()) {
                btn_switch.setText("停止盘点倒计时" + "(" + millisUntilFinished / 1000 + ") ");
            }
        }

        @Override
        public void onFinish() {
            if (btn_switch != null) {
                btn_switch.setText("开始盘点");
                btn_switch.setChecked(false);
            }
            MyApplication.sv_Main.DoInventoryTag(false);
//            closeRfid();
        }
    }
    //创建handler
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyApplication.sv_Main.DoInventoryTag(true);
        }
    };
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public boolean onBackPressedSupport() {
        return showTipsDialog();
    }

    private boolean showTipsDialog() {
        if (mAdapter.getData().size() == 0) {
            //更新数量 number>0更新，否则删除 放在数据库处理了
            new DbManager().upDateNumberTaskBeanWhereId(taskid, mAdapter.getData().size());
            pop();
            return true;
        }
        new AppDialog(_mActivity, DialogType.DEFAULT).setContent("是否保存数据并返回？")
                .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                    @Override
                    public void onClick(String val) {
                    }
                })
                .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                    @Override
                    public void onClick(String val) {
                        //更新数量 number>0更新，否则删除 放在数据库处理了
                        new DbManager().upDateNumberTaskBeanWhereId(taskid, mAdapter.getData().size());
                        pop();
                    }
                })
                .show();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        time.onFinish();
        time=null;
        EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_CUR);
    }

    private void openRfid() {
        //开启服务
        boolean b = MyApplication.sv_Main.Create(OnMsg);
        //先关闭盘点
        MyApplication.sv_Main.DoInventoryTag(false);
        // rfid power on
        MyApplication.sv_Main.gpio_rfid_config(true);
        handler.sendMessageDelayed(new Message(),2000);

    }

    private void closeRfid() {
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
    }

    //接受终端返回的数据
    @Subscriber(tag = EventBusTags.UPLOADDATA)
    private void upload(Event event) {
        //位于栈顶才接收
        if (getTopFragment() instanceof NewInventoryFragment) {
            if (event.getAction() == "1") {
                showToast(ToastUtil.TPYE_FAILURE, "上传成功");
                //上传修改数据库任务状态
                taskBean.setPassFlag(passFlag);
                taskBean.setReason(reason);
                taskBean.setState(1);
                taskBean.setNumber(mAdapter.getData().size());
                new DbManager().upDateTaskBeanWhereId(taskBean.getId(), taskBean);
                EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_HIS);
            } else {
                showToast(ToastUtil.TPYE_FAILURE, "上传失败");
            }
        }
    }

    //接口接收
    @Override
    public void receiveResult(ArrayList<AssetsBean> list) {
        if (list.size() > 0) {
            ChangeData(list);
        }
    }

    @Override
    public void upLoadResult() {
        Toast.makeText(_mActivity, "提交成功", Toast.LENGTH_SHORT).show();
//上传修改数据库任务状态   passFlag reason
        taskBean.setPassFlag(passFlag);
        taskBean.setReason(reason);
        taskBean.setState(1);
        taskBean.setNumber(mAdapter.getData().size());
        new DbManager().upDateTaskBeanWhereId(taskBean.getId(), taskBean);
        EventBusUtils.sendEvent(new Event(""), EventBusTags.REFRESH_HIS);
        pop();
    }

    //接受终端返回的数据
    @Subscriber(tag = EventBusTags.SEARCH_RFID)
    private void updateUser(Event event) {
        //位于栈顶才接收
        Log.e("SEARCH_RFID:", "SEARCH_RFID");
        if (getTopFragment() instanceof NewInventoryFragment) {
            List<AssetsBean> list = (List<AssetsBean>) event.getData();
            ChangeData(list);
        }
    }

    //接受手持机扫描  RFID
    @Subscriber(tag = EventBusTags.SCANRFID)
    private void scaanRfid(Event event) {
        //位于栈顶才接收
        Log.e("SCANRFID:", "SCANRFID");
        if (getTopFragment() instanceof NewInventoryFragment) {
            List<AssetsBean> addData = new ArrayList<>();
            RfidData rfidData = (RfidData) event.getData();
            AssetsBean assetsBean = new AssetsBean(null, null, rfidData.getTagid(), taskid, rfidData.getTagType(), "", "", "", "", "", "", DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL), "", "", "", "", 4, 0);
            assetsBean.setTime(System.currentTimeMillis());
            addData.add(assetsBean);
            if (new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskid, rfidData.getTagid()) != null) {
                return;
            }
            //再插入数据
            new DbManager().insertAssetsBean(assetsBean);
            getDate();

            //播放提示音并震动
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(_mActivity, notification);
            r.play();
            Vibrator vibrator = (Vibrator) _mActivity.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            if (Constants.ISNETORSOCKET) {
                if (CommonUtils.isNetworkConnected()) {
                    mPresenter.getListByRfid(InstructUtils.getUpAssetsBean(addData));
                } else {
                    RfidDataUtils.changeOffline(addData, taskid);
                    getDate();
                }
            } else {
                //判断是否离线，离线访问离线数据库，不是离线，访问请求
                if (UsbUtils.getUsbType()) {
                    InstructUtils.send(InstructUtils.getRFIDMessage(addData));
                } else {
                    RfidDataUtils.changeOffline(_mActivity, addData, taskid);
                    getDate();
                }
            }


        }
    }

    //网络
    @Subscriber(tag = EventBusTags.NETWORKCHANGE)
    private void networkchange(Event event) {
        //位于栈顶才接收
        if (getTopFragment() instanceof NewInventoryFragment) {
            if (event.getAction().equals("NOCONNECT")) {
                new AppDialog(_mActivity)
                        .setTitle("提示")
                        .setContent("以太网连接断开，将进行离线模式")
                        .setSingleButton()
                        .show();
            } else if (event.getAction().equals("NET_ETHERNET")) {
                ToastUtil.showToast("已连接以太网");
            }
        }
    }
}
