package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.AssetsDetailsModule;
import com.idealbank.module_main.mvp.contract.AssetsDetailsContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.AssetsDetailsFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/04/2019 10:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = AssetsDetailsModule.class,  dependencies = AppComponent.class)
public interface AssetsDetailsComponent {
    void inject(AssetsDetailsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AssetsDetailsComponent.Builder view(AssetsDetailsContract.View view);

        AssetsDetailsComponent.Builder appComponent(AppComponent appComponent);

        AssetsDetailsComponent build();
    }
}