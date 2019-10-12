package com.idealbank.module_main.mvp.ui.fragment;

import android.graphics.Color;
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
import com.idealbank.module_main.R2;

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
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.rfid_module.ONDEVMESSAGE;
import com.idealbank.module_main.app.rfid_module.RfidData;
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
    @Inject
    NewInventoryAdapter mAdapter;

    @Inject
    List<AssetsBean> mList;
    String taskid;
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
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        mList = mAdapter.getData();

        taskid = DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_ALL);
        createTime = DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL);
        tv_time.setText(createTime);
        //增加任务
        TaskBean taskBean = new TaskBean();
        taskBean.setState(0);
        taskBean.setTaskid(taskid);
        taskBean.setNumber(mAdapter.getData().size());
        taskBean.setCreateTime(createTime);
        taskBean.setFinishTime(DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_TOTAL));
        new DbManager().insertTaskBean(taskBean);

        //开启服务
        boolean b = MyApplication.sv_Main.Create(OnMsg);
        //先关闭盘点
        MyApplication.sv_Main.DoInventoryTag(false);
        // rfid power on
        MyApplication.sv_Main.gpio_rfid_config(true);
    }


    int a = 0;

    //
    @OnClick({R2.id.btn_add, R2.id.btn_edit, R2.id.btn_query, R2.id.btn_finish, R2.id.btn_refuse, R2.id.btn_allow})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_finish) {
            ll_bottom.setVisibility(View.VISIBLE);
            btn_finish.setVisibility(View.GONE);
        } else if (id == R.id.btn_refuse) {
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择拒绝通行理由").setItemType(AppDialog.REFUSE).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {

                @Override
                public void onClick(String val) {
//                    InstructUtils.send(InstructUtils.getUPLOADDATAMessage(createTime,val,mAdapter.getData()));
                    mPresenter.saveCheckTask(InstructUtils.getUpLoadAssetsBean( createTime,val,mAdapter.getData()));
                }
            }).show();
        } else if (id == R.id.btn_allow) {
            new AppDialog(_mActivity, DialogType.SINGLECHOICE).setTitle("请选择允许通行理由").setItemType(AppDialog.ALLOW).setRightButton("确定", new AppDialog.OnSingleSelectButtonClickListener() {
                @Override
                public void onClick(String val) {
//                    InstructUtils.send(InstructUtils.getUPLOADDATAMessage(createTime,val,mAdapter.getData()));
                    mPresenter.saveCheckTask(InstructUtils.getUpLoadAssetsBean( createTime,val,mAdapter.getData()));
                }
            }).show();
        } else if (id == R.id.btn_edit) {
            new AppDialog(getContext(), DialogType.INPUT)
                    .setTitle("修改项")
                    .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                        @Override
                        public void onClick(String val) {
                        }
                    })
                    .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                        @Override
                        public void onClick(String val) {
                            if (val != null && !val.equals("")) {
                                AssetsBean assets = mList.get(Integer.valueOf(val));
                                assets.setPermissionState(1);
                                mAdapter.setData(Integer.valueOf(val), assets);
                            }
                        }
                    })
                    .show();
        } else if (id == R.id.btn_add) {
            List<AssetsBean> addData = new ArrayList<>();
            AssetsBean assetsBean = new AssetsBean(null, null, "100" + (a + 0), taskid, 0, "", "", "", "", "", "", "", "", "", "", "", 4, 0);
            assetsBean.setTime(System.currentTimeMillis());
            addData.add(assetsBean);
            a = a + 1;
            //再插入数据
            new DbManager().insertInTxAssetsBean(addData);
            getDate();
            //判断是否离线，离线访问离线数据库，不是离线，访问请求

//            if (UsbUtils.getUsbType()) {
//                InstructUtils.send(InstructUtils.getRFIDMessage(addData));
//
//            } else {
//                RfidDataUtils.changeOffline(_mActivity, addData, taskid);
//                getDate();
//            }
            mPresenter.getListByRfid(InstructUtils.getUpAssetsBean(addData));


        } else if (id == R.id.btn_query) {
            //模拟离线查询
            if (UsbUtils.getUsbType()) {
                List<AssetsBean> addData = new DbManager().queryAssetsBeanWhereTaskid(taskid);
                for (int i = 0; i < addData.size(); i++) {
                    AssetsBean assetsBean = addData.get(i);
                    if (i == 0) {
                        assetsBean.setPermissionState(0);
                        assetsBean.setBelongDept("技术");
                    } else if (i == 1) {
                        assetsBean.setPermissionState(1);
                        assetsBean.setBelongDept("香炉");
                    } else if (i == 2) {
                        assetsBean.setPermissionState(3);
                        assetsBean.setBelongDept("智能");
                    }
                }
                EventBusUtils.sendEvent(new Event(addData), EventBusTags.SEARCH_RFID);
            } else {
                changeOffline(mList);
            }
        }


    }

    private void getDate() {
        mList = new DbManager().queryAssetsBeanWhereTaskid(taskid);
        mAdapter.replaceData(mList);
        tv_num.setText("共" + mAdapter.getData().size() + "件物品");
    }


    //处理离线
    private void changeOffline(List<AssetsBean> list) {
        for (int i = 0; i < list.size(); i++) {
            //数据库查询   rfid  离线数据
            OffLineAssetsBean offLineAssetsBean = new DbManager().queryOffLineAssetsBeanWhereRfid(list.get(i).getRfidId());
            AssetsBean assetsBean = new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskid, list.get(i).getRfidId());

            //判断数据库是否有这个数据，有则判断时间，没有则直接不授权
            if (offLineAssetsBean == null) {
                assetsBean.setPermissionState(3);
            } else {
                //根据时间判断  逾期 设置1 未逾期0
                assetsBean.setPermissionState(DateUtils.compartoNow(offLineAssetsBean.getEndTime()) ? 1 : 0);
                assetsBean.setAssetUser(offLineAssetsBean.getAssetUser());
                assetsBean.setBelongDept(offLineAssetsBean.getBelongDept());
                assetsBean.setLastApproveUser(offLineAssetsBean.getLastApproveUser());
                assetsBean.setId(offLineAssetsBean.getId());
                assetsBean.setOutBillId(offLineAssetsBean.getOutBillId());
                assetsBean.setAssetId(offLineAssetsBean.getAssetId());
                assetsBean.setAssetState(offLineAssetsBean.getAssetState());
                assetsBean.setEndTime(offLineAssetsBean.getEndTime());
                assetsBean.setAssetName(offLineAssetsBean.getAssetName());
                assetsBean.setTypeId(offLineAssetsBean.getTypeId());
                assetsBean.setAssetBrand(offLineAssetsBean.getAssetBrand());
                assetsBean.setAssetModel(offLineAssetsBean.getAssetModel());
            }

            new DbManager().upAssetsBeanWhereId(assetsBean);
            getDate();
        }

    }


    @Override
    protected void initEventAndData() {

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

    //接受手持机扫描
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
            //判断是否离线，离线访问离线数据库，不是离线，访问请求
            if (UsbUtils.getUsbType()) {
//                Message message = new Message();
//                message.setId(1);
//                message.setType(MsgType.RFID);
//                UpLoad upLoad = new UpLoad();
//                upLoad.setId("1");
//                upLoad.setDeviceId("");
//                upLoad.setCreateTime(createTime);
//                upLoad.setAssetList(addData);
//                message.setResponseMessage(upLoad);
//                InstructUtils.send(message);

                UpAssetsBean upAssetsBean = new UpAssetsBean();
                UpAssetsBean.RfidIdBean.ResponseMessageBean responseMessageBean = new UpAssetsBean.RfidIdBean.ResponseMessageBean();
                responseMessageBean.setAssetList(addData);
                UpAssetsBean.RfidIdBean rfidIdBean = new UpAssetsBean.RfidIdBean();
                rfidIdBean.setId(1);
                rfidIdBean.setResponseMessage(responseMessageBean);
                upAssetsBean.setRfidId(rfidIdBean);

                mPresenter.getListByRfid(upAssetsBean);

            } else {
                changeOffline(addData);
            }
        }
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
//       
        ArmsUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getActivity()));
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
                                    new DbManager().delAssetsBeanWhereId(mList.get(position).getUid());
                                    ToastUtil.showToast("删除成功");
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
                    Message message = new Message();
                    message.setId(1);
                    message.setType(MsgType.RFID);
                    UpLoad upLoad = new UpLoad();
                    upLoad.setId("1");
                    upLoad.setDeviceId("");
                    upLoad.setCreateTime(createTime);
                    upLoad.setAssetList(addData);
                    message.setResponseMessage(upLoad);
                    InstructUtils.send(message);
                } else {
                    changeOffline(addData);
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

    //接受终端返回的数据
    @Subscriber(tag = EventBusTags.UPLOADDATA)
    private void upload(Event event) {
        //位于栈顶才接收
        if (getTopFragment() instanceof NewInventoryFragment) {
            if (event.getAction() == "1") {
                ToastUtil.showToast(ToastUtil.TPYE_FAILURE, "上传成功");
            } else {
                ToastUtil.showToast(ToastUtil.TPYE_FAILURE, "上传失败");
            }
        }
    }


}
