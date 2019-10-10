package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.SettingIpModule;
import com.idealbank.module_main.mvp.contract.SettingIpContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.SettingIpFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/26/2019 15:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SettingIpModule.class, dependencies = AppComponent.class)
public interface SettingIpComponent {
    void inject(SettingIpFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SettingIpComponent.Builder view(SettingIpContract.View view);

        SettingIpComponent.Builder appComponent(AppComponent appComponent);

        SettingIpComponent build();
    }
}