package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.mvp.ui.adapter.HomeAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerTestComponent;
import com.idealbank.module_main.mvp.contract.TestContract;
import com.idealbank.module_main.mvp.presenter.TestPresenter;

import com.idealbank.module_main.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseRootFragment;
import me.jessyan.armscomponent.commonsdk.utils.CommonUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/21/2019 15:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TestFragment extends BaseRootFragment<TestPresenter> implements TestContract.View  , CompoundButton.OnCheckedChangeListener{
    @BindView(R2.id.normal_view)
    SmartRefreshLayout srl_smart_refresh;
    @BindView(R2.id.knowledge_hierarchy_recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    HomeAdapter mAdapter;
//    @Inject
//    MyApplication mApplication;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    protected void reload() {
        mPresenter.requestGirls(true);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        srl_smart_refresh.finishRefresh();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    @Override
    protected void initEventAndData() {
        MyApplication.mComponent.getAppDataManager().addHistoryData("442323");

//        appDataManager.addHistoryData("212");
        Log.e("ss",""+ MyApplication.mComponent.getAppDataManager().loadAllHistoryData().size());

//        mApplication.appDataManager.addHistoryData("2222");
        initRecyclerView();
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.requestGirls(true);
        if (CommonUtils.isNetworkConnected()) {
            showLoad();
        }else {
            showError();
        }
        mCbSettingNight.setChecked(false);
        mCbSettingNight.setOnCheckedChangeListener(this);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        srl_smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.requestGirls(true);
            }
        });
        srl_smart_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPresenter.requestGirls(false);
            }
        });
//        srl_smart_refresh.autoRefresh();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                showLoad();
            }
        });

    }
    @BindView(R2.id.cb_setting_night)
    AppCompatCheckBox mCbSettingNight;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.cb_setting_night) {
//            MyApplication.setDefaultNightMode(isChecked?1:2);
            ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(isChecked?1:2);
            getActivity().recreate();
        } else {
        }
    }
    @Override
    public void startLoadMore() {

    }

    @Override
    public void showView() {
        showNormal();
    }

    @Override
    public void endLoadMore() {
        srl_smart_refresh.finishLoadMore();
    }

    @Override
    public void noLoadMore() {
        srl_smart_refresh.finishLoadMoreWithNoMoreData();
    }
}
