package com.idealbank.module_main.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.idealbank.module_main.R2;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.bean.LoginBeanRequest;
import com.idealbank.module_main.mvp.model.api.Api;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.idealbank.module_main.di.component.DaggerSettingIpComponent;
import com.idealbank.module_main.mvp.contract.SettingIpContract;
import com.idealbank.module_main.mvp.presenter.SettingIpPresenter;

import com.idealbank.module_main.R;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonres.dialog.DialogType;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class SettingIpFragment extends BaseActionBarFragment<SettingIpPresenter> implements SettingIpContract.View {


    @BindView(R2.id.edt_ip)
    EditText mEdtIP;
    @BindView(R2.id.edt_port)
    EditText mEdtPort;

    public static SettingIpFragment newInstance() {
        SettingIpFragment fragment = new SettingIpFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSettingIpComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_ip, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        setTitleText("服务器IP设置");
        mEdtIP.setText( new DbManager().getIp()==""?Constants.IP:new DbManager().getIp());
        mEdtPort.setText( new DbManager().getPort()==""?Constants.PORT:new DbManager().getPort());
    }

    @OnClick({R2.id.btn_edit})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_edit) {
            new DbManager().setIp( mEdtIP.getText().toString());
            new DbManager().setPort( mEdtPort.getText().toString());
            ToastUtil.showToast(ToastUtil.TPYE_SUCCESS, "修改成功");
//            new AppDialog(_mActivity, DialogType.LOGIN).setTitle("登录")
//                    .setLeftButton("取消", new AppDialog.OnButtonClickListener() {
//                        @Override
//                        public void onClick(String val) {
//                        }
//                    })
//                    .setRightButton("确定", new AppDialog.OnLoginButtonClickListener() {
//                                @Override
//                                public void onClick(String account, String pwd) {
//                                    mPresenter.login(new LoginBeanRequest(account, pwd));
//                                }
//                            }
//
//                    )
//                    .show();
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

    @Override
    protected void initEventAndData() {

    }


    @Override
    public void loginResult() {
        new DbManager().setIp( mEdtIP.getText().toString());
        new DbManager().setPort( mEdtPort.getText().toString());
        ToastUtil.showToast(ToastUtil.TPYE_SUCCESS, "修改成功");
    }
}
