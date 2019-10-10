package com.idealbank.module_main.di.module;

import com.idealbank.module_main.mvp.ui.adapter.HistoryAssetsAdapter;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;

import com.idealbank.module_main.mvp.contract.HistoricalInventoryDetailsContract;
import com.idealbank.module_main.mvp.model.HistoricalInventoryDetailsModel;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 14:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class HistoricalInventoryDetailsModule {

    @Binds
    abstract HistoricalInventoryDetailsContract.Model bindHistoricalInventoryDetailsModel(HistoricalInventoryDetailsModel model);


    @FragmentScope
    @Provides
    static List<AssetsBean> provideList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static HistoryAssetsAdapter provideHistoryAssetsAdapterAdapter(List<AssetsBean> list) {
        return new HistoryAssetsAdapter(list);
    }









}