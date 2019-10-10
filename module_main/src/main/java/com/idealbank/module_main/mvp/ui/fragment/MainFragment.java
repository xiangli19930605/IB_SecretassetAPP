package com.idealbank.module_main.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealbank.module_main.Netty.NetEvent;
import com.idealbank.module_main.R;
import com.idealbank.module_main.mvp.ui.view.BottomBar;
import com.idealbank.module_main.mvp.ui.view.BottomBarTab;
import com.jess.arms.di.component.AppComponent;

import org.simple.eventbus.Subscriber;

import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class MainFragment extends BaseFragment {
    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FORTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    public static BottomBar mBottomBar;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = CurrentInventoryFragment.newInstance();
            mFragments[THIRD] = HistoricalInventoryFragment.newInstance();
            mFragments[FORTH] = ForthFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[FORTH],
                    mFragments[THIRD]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(CurrentInventoryFragment.class);
            mFragments[THIRD] = findChildFragment(HistoricalInventoryFragment.class);
            mFragments[FORTH] = findChildFragment(ForthFragment.class);
        }
    }

    private void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.bg_home_uncheck, getString(R.string.home_pager)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.bg_task_uncheck, getString(R.string.title_shop)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.bg_history_uncheck, getString(R.string.title_projects)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.bg_me_uncheck, getString(R.string.title_discover)));

        // 模拟未读消息
//        mBottomBar.getItem(FIRST).setUnreadCount(9);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                Log.e("position:",""+position+"prePosition:"+prePosition);
                showHideFragment(mFragments[position], mFragments[prePosition]);

//                BottomBarTab tab = mBottomBar.getItem(FIRST);
//                if (position == FIRST) {
//                    tab.setUnreadCount(0);
//                } else {
//                    tab.setUnreadCount(tab.getUnreadCount() + 1);
//                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }
    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }
    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    @Subscriber(tag = EventBusTags.JUMP_PAGE)
    private void updateUser(Event event) {
        if(event.getAction()==EventBusTags.ONE){
            mBottomBar.setCurrentItem(1);
        }else if(event.getAction()==EventBusTags.TWO){
            mBottomBar.setCurrentItem(2);
        }else if(event.getAction()==EventBusTags.ZERO){
            mBottomBar.setCurrentItem(0);
        }else if(event.getAction()==EventBusTags.THREE){
            mBottomBar.setCurrentItem(3);
        }
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.wechat_fragment_main, container, false);
        initView(view);
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {
        Log.e("222","222");


    }
    public int getCurrent() {
        return mBottomBar.getCurrentItemPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBottomBar=null;
    }
}
