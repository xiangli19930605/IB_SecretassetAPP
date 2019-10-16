package com.idealbank.module_main.mvp.presenter;

import android.app.Application;

import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.bean.UpLoadAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.idealbank.module_main.mvp.contract.NewInventoryContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/21/2019 15:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class NewInventoryPresenter extends BasePresenter<NewInventoryContract.Model, NewInventoryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public NewInventoryPresenter(NewInventoryContract.Model model, NewInventoryContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    public void getListByRfid(UpAssetsBean task) {
        RetrofitUrlManager.getInstance().putDomain(Constants.WANGYI_DOMAIN_NAME, "http://" + new DbManager().getIp()+ ":" + new DbManager().getPort());
        mModel.getListByRfid(task)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponseBean<ArrayList<AssetsBean>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponseBean<ArrayList<AssetsBean>> assetsBean) {
                        mRootView.receiveResult(assetsBean.getData());
                    }
                });
    }

    public void saveCheckTask(UpLoadAssetsBean upLoadAssetsBean) {
        RetrofitUrlManager.getInstance().putDomain(Constants.WANGYI_DOMAIN_NAME, "http://" + new DbManager().getIp()+ ":" + new DbManager().getPort());
        mModel.saveCheckTask(upLoadAssetsBean)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponseBean>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponseBean assetsBean) {
                        if (assetsBean.getState()==1) {
                            mRootView.upLoadResult();
                        }else{
                            ArmsUtils.snackbarText(assetsBean.getMessage());
                        }
                    }
                });
    }


}
