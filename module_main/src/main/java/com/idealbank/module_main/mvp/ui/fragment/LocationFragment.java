package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.mvp.ui.adapter.MyAdapter;
import com.idealbank.module_main.mvp.ui.adapter.SpinnerLocationAdapter;
import com.idealbank.module_main.mvp.ui.view.CustomPopWindow;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerLocationComponent;
import com.idealbank.module_main.mvp.contract.LocationContract;
import com.idealbank.module_main.mvp.presenter.LocationPresenter;

import com.idealbank.module_main.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.autosize.utils.LogUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:位置修改
 * <p>
 * Created by MVPArmsTemplate on 10/10/2019 13:33
 * ================================================
 */
public class LocationFragment extends BaseActionBarFragment<LocationPresenter> implements LocationContract.View {
    @BindView(R2.id.tV_location)
    TextView tvLocation;

    boolean isSpinnerFirst = true;
    SpinnerLocationAdapter mAdapter;
    Location selectedItemperson;
    private ListPopupWindow popupWindow;

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerLocationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitleText("地址设置");




        mPresenter.getLocationList();
        selectedItemperson = GsonUtil.GsonToBean(new DbManager().getLocation(), Location.class);
        if (selectedItemperson != null) {
            LogUtils.e(selectedItemperson.getSpaceName());
            tvLocation.setText(selectedItemperson.getSpaceName());
        }


    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.btn_confim, R2.id.ll_spinner})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_confim) {
            if (!(selectedItemperson == null)) {
                new DbManager().setLocation(GsonUtil.GsonString(selectedItemperson));
                ToastUtil.showToast(ToastUtil.TPYE_SUCCESS, "修改成功");
            }
        } else if (id == R.id.ll_spinner) {
            showPopListView();
        }
    }

    CustomPopWindow mListPopWindow;

    private void showPopListView() {
        View contentView = LayoutInflater.from(_mActivity).inflate(R.layout.pop_list, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(_mActivity)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .create()
                .showAsDropDown(tvLocation, 0, 20);
    }

    MyAdapter myAdapter = new MyAdapter(new ArrayList());

    private void handleListView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        myAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                selectedItemperson=myAdapter.getData().get(position);
                tvLocation.setText(selectedItemperson.getSpaceName());
                mListPopWindow.dissmiss();
            }
        });
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void getLocation(ArrayList<Location> list) {
        myAdapter.replaceData(list);
    }
}
