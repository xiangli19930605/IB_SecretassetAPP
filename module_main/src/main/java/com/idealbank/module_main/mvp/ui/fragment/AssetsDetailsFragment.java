package com.idealbank.module_main.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.idealbank.module_main.R;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.di.component.DaggerAssetsDetailsComponent;
import com.idealbank.module_main.mvp.contract.AssetsDetailsContract;
import com.idealbank.module_main.mvp.presenter.AssetsDetailsPresenter;
import com.jess.arms.di.component.AppComponent;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;


/**
 * ================================================
 * Description:资产详情
 * <p>
 * Created by MVPArmsTemplate on 03/04/2019 10:26
 * ================================================
 */
public class AssetsDetailsFragment extends BaseActionBarFragment<AssetsDetailsPresenter> implements AssetsDetailsContract.View {


    @BindView(R2.id.stv_rfidId)
    SuperTextView stvRfidId;
    @BindView(R2.id.stv_belongDept)
    SuperTextView stvBelongDept;
    @BindView(R2.id.stv_assetUser)
    SuperTextView stvAssetUser;
    @BindView(R2.id.stv_permissionState)
    SuperTextView stvPermissionState;
    @BindView(R2.id.stv_assetState)
    SuperTextView stvAssetState;
    @BindView(R2.id.stv_assetModel)
    SuperTextView stvAssetModel;

    AssetsBean assetsBean;
    public static AssetsDetailsFragment newInstance(AssetsBean assetsBean) {
        AssetsDetailsFragment fragment = new AssetsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("assetsBean", (Serializable) assetsBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerAssetsDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets_details, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitleText("物品详情");
        assetsBean = (AssetsBean) getArguments().getSerializable("assetsBean");
        stvRfidId.setCenterString(assetsBean.getRfidId());
        stvBelongDept.setCenterString(assetsBean.getBelongDept());
        stvAssetUser.setCenterString(assetsBean.getAssetUser());
        stvAssetModel.setCenterString(assetsBean.getAssetModel());

//        int permissionState=4;  //0 已授权     1 未授权 2     3      4查询中
//        int assetState=0;    //0   待返回 1  已返回   2不需要返回

        if(assetsBean.getAssetState()==0){
            stvAssetState.setCenterString("待返回");
        }else if(assetsBean.getAssetState()==1){
            stvAssetState.setCenterString("已返回");
        }else if(assetsBean.getAssetState()==2){
            stvAssetState.setCenterString("不需要返回");
        }
        if(assetsBean.getPermissionState()==0){
            stvPermissionState.setCenterString("已授权");
        }else if(assetsBean.getPermissionState()==1){
            stvPermissionState.setCenterString("未授权");
        }else if(assetsBean.getPermissionState()==4){
            stvPermissionState.setCenterString("查询中");
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

    @Subscriber(tag = EventBusTags.SEARCH_RFID2)
    private void updateUser(Event event) {
        if (getTopFragment() instanceof AssetsDetailsFragment) {
//            start(HistoricalInventoryDetailsFragment.newInstance());
        }
    }


}
