package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.OfflineDataModule;
import com.idealbank.module_main.mvp.contract.OfflineDataContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.OfflineDataFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/15/2019 09:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = OfflineDataModule.class, dependencies = AppComponent.class)
public interface OfflineDataComponent {
    void inject(OfflineDataFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OfflineDataComponent.Builder view(OfflineDataContract.View view);

        OfflineDataComponent.Builder appComponent(AppComponent appComponent);

        OfflineDataComponent build();
    }
}