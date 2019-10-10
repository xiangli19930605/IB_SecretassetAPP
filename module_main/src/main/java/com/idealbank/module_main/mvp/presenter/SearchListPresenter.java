package com.idealbank.module_main.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.idealbank.module_main.mvp.ui.adapter.SearchListAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.BaseResponseBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.RxUtil;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.idealbank.module_main.mvp.contract.SearchListContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 09:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class SearchListPresenter extends BasePresenter<SearchListContract.Model, SearchListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    SearchListAdapter mAdapter;
    @Inject
    List<HistoryData> mList;
    @Inject
    public SearchListPresenter(SearchListContract.Model model, SearchListContract.View rootView) {
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
    public void loadAllHistoryData() {
        mList= mModel.loadAllHistoryData();
        Collections.reverse(mList);
        mAdapter.replaceData(mList);
//        Observable.create((ObservableOnSubscribe<List<HistoryData>>) e -> {
//            List<HistoryData> historyDataList = (List<HistoryData>)
//                    MyApplication.mComponent.getAppDataManager().loadAllHistoryData();
//            e.onNext(historyDataList);
//        })
//                .compose(RxUtil.rxSchedulerHelper())
//                .subscribe(historyDataList ->
//
////                        mList=historyDataList
////                        mAdapter.replaceData(mList);
//                        mRootView.loadAllHistoryData(historyDataList)
// );
    }

    public void addHistoryData(String data) {
        MyApplication.mComponent.getAppDataManager().addHistoryData(data);
        loadAllHistoryData();
    }

    public void clearHistoryData() {
        MyApplication.mComponent.getAppDataManager().clearHistoryData();
        loadAllHistoryData();
    }


    public void getListByRfid(UpAssetsBean task) {
        RetrofitUrlManager.getInstance().putDomain(Constants.WANGYI_DOMAIN_NAME, "http://"+ Constants.IP+":"+Constants.PORT);
        mModel.getListByRfid(task)
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
                .subscribe(new ErrorHandleSubscriber<BaseResponseBean<UpLoad>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponseBean<UpLoad> assetsBean) {


                    }
                });
    }




}
