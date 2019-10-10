package com.idealbank.module_main.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.idealbank.module_main.mvp.model.entity.User;
import com.idealbank.module_main.mvp.ui.adapter.HomeAdapter;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.idealbank.module_main.mvp.contract.TestContract;
import com.idealbank.module_main.mvp.model.TestModel;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/21/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class TestModule {

    @Binds
    abstract TestContract.Model bindTestModel(TestModel model);
    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(TestContract.View view) {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @FragmentScope
    @Provides
    static List<User> provideList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static HomeAdapter provideSmartRefreshAdapter(List<User> list) {
        return new HomeAdapter(list);
    }



//    @Provides
//    @FragmentScope
//    static DbHelper provideDbHelper(DbHelperImpl appDbHelper) {
//        return  appDbHelper;
//    }



}