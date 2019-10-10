package com.idealbank.module_main.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.idealbank.module_main.mvp.ui.adapter.SearchListAdapter;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.greendao.DaoSession;

import com.idealbank.module_main.mvp.contract.SearchListContract;
import com.idealbank.module_main.mvp.model.SearchListModel;

import java.util.ArrayList;
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
@Module
public abstract class SearchListModule {

    @Binds
    abstract SearchListContract.Model bindSearchListModel(SearchListModel model);


    @FragmentScope
    @Provides
    static List<HistoryData> provideList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static SearchListAdapter provideSearchListAdapter(List<HistoryData> list) {
        return new SearchListAdapter(list);
    }

}