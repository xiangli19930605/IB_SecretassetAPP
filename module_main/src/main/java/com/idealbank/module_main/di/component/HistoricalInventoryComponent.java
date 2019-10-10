package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.HistoricalInventoryModule;
import com.idealbank.module_main.mvp.contract.HistoricalInventoryContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.HistoricalInventoryFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 14:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = HistoricalInventoryModule.class, dependencies = AppComponent.class)
public interface HistoricalInventoryComponent {
    void inject(HistoricalInventoryFragment fragment);


    @Component.Builder
    interface Builder {
        @BindsInstance
        HistoricalInventoryComponent.Builder view(HistoricalInventoryContract.View view);

        HistoricalInventoryComponent.Builder appComponent(AppComponent appComponent);

        HistoricalInventoryComponent build();
    }
}