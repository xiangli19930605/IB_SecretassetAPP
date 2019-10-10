package com.idealbank.module_main.mvp.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.idealbank.module_main.R2;
import com.jess.arms.di.component.AppComponent;

import com.idealbank.module_main.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseActionBarFragment;
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


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
public class ReadTagFragment extends BaseFragment {

    @BindView(R2.id.quyu_read_data_input)
    Spinner quyu_read_data_input;
    @BindView(R2.id.zhizhenchangdu_read_data_input)
    EditText zhizhenchangdu_read_data_input;
    @BindView(R2.id.zicahgndu_read_data_input)
    EditText zicahgndu_read_data_input;
    @BindView(R2.id.read_dataOder_input1)
    EditText read_dataOder_input1;
    @BindView(R2.id.sendmessage_button)
    Button sendmessage_button;
    @BindView(R2.id.read_data_button)
    Button read_data_button;
    @BindView(R2.id.textView_read_data)
    TextView textView_read_data;

    private int begin;
    private int data;
    private String password;
    static String tag;

    public static ReadTagFragment newInstance(String rfid) {
        ReadTagFragment fragment = new ReadTagFragment();
        tag = rfid;
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read_tag, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //先开启认证
        MyApplication.sv_Main.DoIndentify(true);
    }

    @OnClick({R2.id.read_data_button, R2.id.sendmessage_button})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.read_data_button) {

        } else if (id == R.id.sendmessage_button) {
// 获得区域信息
            String quyu_data = quyu_read_data_input.getSelectedItem()
                    .toString();
            int quyuId = 3;
            if (quyu_data.equals("用户区")) {
                quyuId = 3;
            } else if (quyu_data.equals("安全区")) {
                quyuId = 2;
            } else if (quyu_data.equals("标签信息区")) {
                quyuId = 0;
            } else if (quyu_data.equals("编码区")) {
                quyuId = 1;
            }
            // 获得起始位置
            try {
                String zhizhenchangdu_data = zhizhenchangdu_read_data_input
                        .getText().toString();
                begin = Integer.parseInt(zhizhenchangdu_data);
            } catch (Exception e) {

            }

            // 获得字长度
            try {
                String zicahgndu_data = zicahgndu_read_data_input.getText()
                        .toString();
                data = Integer.parseInt(zicahgndu_data);
            } catch (Exception e) {

            }
            // 操作密码
            try {
                password = read_dataOder_input1.getText().toString();
            } catch (Exception e) {

            }
            /**
             * 发送指令
             */
            try {
                MyApplication.sv_Main.DoReadTagInfo(tag, quyuId, begin + 1, data, password);
                // sv_Read.DoReadTagInfo("A09000000000C509", 3, 0, 4, "0000");
            } catch (Exception e) {
                AlertDialog Al = new AlertDialog.Builder(
                        _mActivity)
                        .setMessage("指令有误，请修改填写内容！")// 内容
                        .create();
                Al.show();
            }
        }


    }

    @Subscriber(tag = EventBusTags.READTAG)
    private void updateUser(Event event) {
        //位于栈顶才接收
        Log.e("READTAG:", "READTAG");
        ToastUtil.showToast((String)event.getData());
        try {
            JSONObject  szCode=  new JSONObject((String)event.getData());
            textView_read_data.setText(szCode.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
