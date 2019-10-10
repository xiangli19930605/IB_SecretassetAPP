package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.idealbank.module_main.mvp.ui.fragment.CurrentInventoryFragment;
import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.CurrentInventoryModule;
import com.idealbank.module_main.mvp.contract.CurrentInventoryContract;

import com.jess.arms.di.scope.FragmentScope;


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
@FragmentScope
@Component(modules = CurrentInventoryModule.class, dependencies = AppComponent.class)
public interface CurrentInventoryComponent {
    void inject(CurrentInventoryFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        CurrentInventoryComponent.Builder view(CurrentInventoryContract.View view);

        CurrentInventoryComponent.Builder appComponent(AppComponent appComponent);

        CurrentInventoryComponent build();
    }
}