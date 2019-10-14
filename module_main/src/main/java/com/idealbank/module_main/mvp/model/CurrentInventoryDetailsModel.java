package com.idealbank.module_main.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.bean.UpLoadAssetsBean;
import com.idealbank.module_main.mvp.model.api.Api;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.idealbank.module_main.mvp.contract.CurrentInventoryDetailsContract;

import java.util.ArrayList;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/06/2019 13:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class CurrentInventoryDetailsModel extends BaseModel implements CurrentInventoryDetailsContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CurrentInventoryDetailsModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
    @Override
    public Observable<BaseResponseBean<ArrayList<AssetsBean>>> getListByRfid(UpAssetsBean task) {
        return mRepositoryManager.obtainRetrofitService(Api.class)
                .getListByRfid( task);
    }
    @Override
    public Observable<BaseResponseBean> saveCheckTask(UpLoadAssetsBean upLoadAssetsBean) {
        return mRepositoryManager.obtainRetrofitService(Api.class)
                .saveCheckTask(upLoadAssetsBean  );
    }
}