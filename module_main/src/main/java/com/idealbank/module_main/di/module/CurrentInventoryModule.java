package com.idealbank.module_main.di.module;

import com.idealbank.module_main.mvp.ui.adapter.CurrentInventoryAdapter;
import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;

import com.idealbank.module_main.mvp.contract.CurrentInventoryContract;
import com.idealbank.module_main.mvp.model.CurrentInventoryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 14:55
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class CurrentInventoryModule {

    @Binds
    abstract CurrentInventoryContract.Model bindCurrentInventoryModel(CurrentInventoryModel model);


    @FragmentScope
    @Provides
    static List<TaskBean> provideList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static CurrentInventoryAdapter provideCurrentInventoryAdapter(List<TaskBean> list) {
        return new CurrentInventoryAdapter(list);
    }
}