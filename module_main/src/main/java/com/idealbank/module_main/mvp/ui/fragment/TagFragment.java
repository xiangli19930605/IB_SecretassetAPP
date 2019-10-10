package com.idealbank.module_main.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealbank.module_main.R;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.mvp.ui.adapter.WechatPagerFragmentAdapter;
import com.jess.arms.di.component.AppComponent;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 09:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TagFragment extends BaseActionBarFragment {

    @BindView(R2.id.main_vp_container)
    ViewPager mViewPager;
    @BindView(R2.id.toolbar_tab)
    TabLayout tabLayout;
    static String tag;
    public static TagFragment newInstance(String rfid) {
        TagFragment fragment = new TagFragment();
        tag = rfid;
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //先开启认证
        MyApplication.sv_Main.DoIndentify(true);

        mViewPager.setAdapter(new WechatPagerFragmentAdapter(getChildFragmentManager(), tag));
        tabLayout.setupWithViewPager(mViewPager);

    }





    @Override
    public void setData(@Nullable Object data) {

    }




    @Override
    protected void initEventAndData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //先开启认证
        MyApplication.sv_Main.DoIndentify(false);
    }
}
