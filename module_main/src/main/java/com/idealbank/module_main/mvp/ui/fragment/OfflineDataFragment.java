package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.OfflineBeanRequest;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerOfflineDataComponent;
import com.idealbank.module_main.mvp.contract.OfflineDataContract;
import com.idealbank.module_main.mvp.presenter.OfflineDataPresenter;

import com.idealbank.module_main.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonres.dialog.LoadingDialog;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * ================================================
 */
public class OfflineDataFragment extends BaseActionBarFragment<OfflineDataPresenter> implements OfflineDataContract.View {
    protected LoadingDialog loadingDialog;
    @BindView(R2.id.stv_time)
    SuperTextView stv_time;
    @BindView(R2.id.stv_num)
    SuperTextView stv_num;
    @BindView(R2.id.txt_tips)
    TextView txt_tips;

    public static OfflineDataFragment newInstance() {
        OfflineDataFragment fragment = new OfflineDataFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerOfflineDataComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline_data, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitleText("获取离线数据");
        loadingDialog = new LoadingDialog(_mActivity);
        List<OffLineAssetsBean> list = new DbManager().loadAllOffLineAssetsBean();
        stv_num.setCenterString("" + list.size());
        stv_time.setCenterString(new DbManager().getUpdataTime());
    }

    @OnClick({R2.id.btn_updata})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_updata) {
            Location location = GsonUtil.GsonToBean(new DbManager().getLocation(), Location.class);
            if (location != null) {
                OfflineBeanRequest offlineBeanRequest = new OfflineBeanRequest();
                offlineBeanRequest.setDeviceId(location.getId());
                offlineBeanRequest.setRfidId("");
                mPresenter.getOffLinePermissionList(offlineBeanRequest);
                if (loadingDialog != null && !loadingDialog.isShowing()) {
                    loadingDialog.setTitleText("请求离线数据").show();
                } else {
                    loadingDialog.show();
                }
            } else {
                new AppDialog(_mActivity, DialogType.DEFAULT).setTitle("未设置通道门位置，是否前去设置？")
                        .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                            }
                        })
                        .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                                start(LocationFragment.newInstance());
                            }
                        })
                        .show();


            }


        }
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

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void getOffLinePermissionList(ArrayList<OffLineAssetsBean> list) {
        dismissLoading();
        new DbManager().clearOffLineAssetsBean();
        new DbManager().insertInTxOffLineAssetsBean(list);
        new DbManager().setUpdataTime(DateUtils.getCurrentDateStr(Constants.DATE_FORMAT_ALL));
        txt_tips.setText("本次更新" + list.size() + "条数据");
        stv_time.setCenterString(new DbManager().getUpdataTime());
        stv_num.setCenterString("" + list.size());
    }
}
