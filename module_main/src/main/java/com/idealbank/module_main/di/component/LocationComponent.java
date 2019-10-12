package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.LocationModule;
import com.idealbank.module_main.mvp.contract.LocationContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.LocationFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/10/2019 13:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = LocationModule.class, dependencies = AppComponent.class)
public interface LocationComponent {
    void inject(LocationFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LocationComponent.Builder view(LocationContract.View view);

        LocationComponent.Builder appComponent(AppComponent appComponent);

        LocationComponent build();
    }
}