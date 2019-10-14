package com.idealbank.module_main.mvp.presenter;

import android.app.Application;

import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.LoginBeanRequest;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.idealbank.module_main.mvp.contract.LocationContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;


/**
 * ================================================
 * Description:位置修改
 * <p>
 */
@FragmentScope
public class LocationPresenter extends BasePresenter<LocationContract.Model, LocationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LocationPresenter(LocationContract.Model model, LocationContract.View rootView) {
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

    public void getLocationList() {
        RetrofitUrlManager.getInstance().putDomain(Constants.WANGYI_DOMAIN_NAME, "http://" + Constants.IP + ":" + Constants.PORT);
        mModel.getLocationList()
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
                .subscribe(new ErrorHandleSubscriber<BaseResponseBean<ArrayList<Location>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponseBean<ArrayList<Location>> assetsBean) {
                        mRootView.getLocation(assetsBean.getData());
                    }
                });
    }
    public void login(LoginBeanRequest loginBeanRequest) {
        RetrofitUrlManager.getInstance().putDomain(Constants.WANGYI_DOMAIN_NAME, "http://" + Constants.IP + ":" + Constants.PORT);
        mModel.login(loginBeanRequest)
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
                    public void onNext(BaseResponseBean bean) {
                        if(bean.getState()==1){
                            mRootView.loginResult();
                        }else{
                            ArmsUtils.snackbarText(bean.getMessage());
                        }
                    }
                });
    }




}
