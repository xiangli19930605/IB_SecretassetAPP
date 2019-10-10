package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.utils.Intents;
import com.idealbank.module_main.mvp.ui.activity.MainActivity;
import com.idealbank.module_main.mvp.ui.activity.TcpLinkActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerForthComponent;
import com.idealbank.module_main.mvp.contract.ForthContract;
import com.idealbank.module_main.mvp.presenter.ForthPresenter;

import com.idealbank.module_main.R;
import com.jess.arms.utils.LogUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.dialog.LoadingDialog;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.armscomponent.commonsdk.utils.Utils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 09:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ForthFragment extends BaseFragment<ForthPresenter> implements ForthContract.View, CompoundButton.OnCheckedChangeListener {
    @BindView(R2.id.cb_setting_night)
    AppCompatCheckBox mCbSettingNight;
    protected LoadingDialog loadingDialog;

    public static ForthFragment newInstance() {
        ForthFragment fragment = new ForthFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerForthComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forth, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mCbSettingNight.setChecked(MyApplication.mComponent.getAppDataManager().getNightModeState());
        mCbSettingNight.setOnCheckedChangeListener(this);
        loadingDialog = new LoadingDialog(_mActivity);


    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.cb_setting_night) {
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            MyApplication.mComponent.getAppDataManager().setNightModeState(isChecked);
            Intent intent = new Intent(_mActivity, MainActivity.class);
            intent.putExtra(EventBusTags.JUMP_PAGE, EventBusTags.THREE);
            startActivity(intent);
            _mActivity.finish();
            _mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
        }
    }


    @OnClick({R2.id.btn_web, R2.id.btn_test, R2.id.btn_offline, R2.id.sv_ip_set})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_web) {
            Intents.getIntents().Intent(getActivity(), TcpLinkActivity.class);
        } else if (i == R.id.btn_test) {
            mPresenter.getLocationList();
        } else if (i == R.id.sv_ip_set) {
            ((MainFragment) getParentFragment()).startBrotherFragment(SettingIpFragment.newInstance());
        } else if (i == R.id.btn_offline) {
//            new DbManager().clearOffLineAssetsBean();
//            ArrayList<OffLineAssetsBean> list=new ArrayList();
//            for (int j = 0; j < 10; j++) {
//                OffLineAssetsBean offLineAssetsBean = new OffLineAssetsBean();
//                offLineAssetsBean.setRfidId("1000" + (j ));
//                offLineAssetsBean.setPermissionState(1);
//                list.add(offLineAssetsBean);
//            }
//            new DbManager().insertInTxOffLineAssetsBean(list);
//           List<OffLineAssetsBean> list2= new DbManager().loadAllOffLineAssetsBean();
//            for (int j = 0; j <list2.size() ; j++) {
//                LogUtils.warnInfo(list2.get(j).getRfidId());
//            }

            if (loadingDialog != null && !loadingDialog.isShowing()) {
                loadingDialog.setTitleText("请求离线数据").show();
            } else {
                loadingDialog.show();
            }
        }
    }

//    @Subscriber(tag = EventBusTags.OFFLINE)
////    private void getOffline(Event event) {
////        dismissLoading();
////        if (loadingDialog != null && !loadingDialog.isShowing()) {
////            loadingDialog.setTitleText("下载离线数据中.....").show();
////        }
////        new DbManager().clearOffLineAssetsBean();
////        List<OffLineAssetsBean> offList = (List<OffLineAssetsBean>) event.getData();
////        new DbManager().insertInTxOffLineAssetsBean(offList);
////        dismissLoading();
////
////    }


    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
