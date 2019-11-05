package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.app.utils.Intents;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.mvp.contract.HomeContract;
import com.idealbank.module_main.mvp.presenter.HomePresenter;
import com.idealbank.module_main.mvp.ui.activity.TcpLinkActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerHomeComponent;

import com.idealbank.module_main.R;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseRootFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.armscomponent.commonsdk.utils.Utils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 首页
 * <p>
 * ================================================
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {
    @BindView(R2.id.btn_search)
    Button btn_search;
    @BindView(R2.id.tv_date)
    TextView tv_date;



    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R2.id.btn_search, R2.id.btn_scan, R2.id.btn_current, R2.id.btn_history})
    void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_search) {
            ((MainFragment) getParentFragment()).startBrotherFragment(SearchListFragment.newInstance());
        } else if (i == R.id.btn_scan) {
            if (Constants.ISNETORSOCKET) {
            Location location = GsonUtil.GsonToBean(new DbManager().getLocation(), Location.class);
            if (location == null) {
                new AppDialog(_mActivity, DialogType.DEFAULT).setTitle("未设置通道门位置，是否前去设置？")
                        .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                            }
                        })
                        .setRightButton("确定", new AppDialog.OnButtonClickListener() {
                            @Override
                            public void onClick(String val) {
                                ((MainFragment) getParentFragment()).startBrotherFragment(LocationFragment.newInstance());
                            }
                        })
                        .show();
            }else{
                ((MainFragment) getParentFragment()).startBrotherFragment(NewInventoryFragment.newInstance());
            }}else{
                ((MainFragment) getParentFragment()).startBrotherFragment(NewInventoryFragment.newInstance());
            }
        } else if (i == R.id.btn_current) {
            EventBusUtils.sendEvent(new Event(EventBusTags.ONE), EventBusTags.JUMP_PAGE);
        } else if (i == R.id.btn_history) {
            EventBusUtils.sendEvent(new Event(EventBusTags.TWO), EventBusTags.JUMP_PAGE);
        }

    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    protected void initEventAndData() {
        Log.e(TAG, "initEventAndData");
//        showLoad();
//        showNormal();
//        showError();
        tv_date.setText(DateUtils.getCurrentDateStr()+"  "+DateUtils.getCurrentWeekStr());

    }


}
