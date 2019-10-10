package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.CurrentInventoryDetailsModule;
import com.idealbank.module_main.mvp.contract.CurrentInventoryDetailsContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.CurrentInventoryDetailsFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/06/2019 13:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = CurrentInventoryDetailsModule.class, dependencies = AppComponent.class)
public interface CurrentInventoryDetailsComponent {
    void inject(CurrentInventoryDetailsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CurrentInventoryDetailsComponent.Builder view(CurrentInventoryDetailsContract.View view);

        CurrentInventoryDetailsComponent.Builder appComponent(AppComponent appComponent);

        CurrentInventoryDetailsComponent build();
    }
}