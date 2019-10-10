package com.idealbank.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.di.module.SearchListModule;
import com.idealbank.module_main.mvp.contract.SearchListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.idealbank.module_main.mvp.ui.fragment.SearchListFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/19/2019 09:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SearchListModule.class, dependencies = AppComponent.class)
public interface SearchListComponent {
    void inject(SearchListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SearchListComponent.Builder view(SearchListContract.View view);

        SearchListComponent.Builder appComponent(AppComponent appComponent);

        SearchListComponent build();
    }
}