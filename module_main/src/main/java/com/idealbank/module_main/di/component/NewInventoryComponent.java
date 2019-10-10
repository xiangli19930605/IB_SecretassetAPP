package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.NewInventoryModule;
import com.idealbank.module_main.mvp.contract.NewInventoryContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.NewInventoryFragment;


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
@Component(modules = NewInventoryModule.class, dependencies = AppComponent.class)
public interface NewInventoryComponent {
    void inject(NewInventoryFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NewInventoryComponent.Builder view(NewInventoryContract.View view);

        NewInventoryComponent.Builder appComponent(AppComponent appComponent);

        NewInventoryComponent build();
    }
}