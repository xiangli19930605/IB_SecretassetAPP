package com.idealbank.module_main.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.OfflineBeanRequest;
import com.idealbank.module_main.mvp.model.api.Api;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.idealbank.module_main.mvp.contract.ForthContract;

import java.util.ArrayList;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;


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
@FragmentScope
public class ForthModel extends BaseModel implements ForthContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ForthModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
    @Override
    public Observable<BaseResponseBean<ArrayList<Location>>> getLocationList(){
        return mRepositoryManager.obtainRetrofitService(Api.class)
                .getLocationList();
    }
    @Override
    public Observable<BaseResponseBean<ArrayList<OffLineAssetsBean>>> getOffLinePermissionList(OfflineBeanRequest offlineBeanRequest){
        return mRepositoryManager.obtainRetrofitService(Api.class)
                .getOffLinePermissionList( offlineBeanRequest);
    }

}