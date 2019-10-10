package com.idealbank.module_main.mvp.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import me.jessyan.armscomponent.commonsdk.base.fragment.BaseRootFragment;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 14:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class WriteTagFragment extends BaseFragment {

    @BindView(R2.id.quyu_write_input)
    Spinner quyu_write_input;
    @BindView(R2.id.zhizhenchangdu_write_input)
    EditText zhizhenchangdu_write_input;
    @BindView(R2.id.writeOder_input1)
    EditText writeOder_input1;
    @BindView(R2.id.writing_input)
    EditText writing_input;
    @BindView(R2.id.textView_write)
    TextView textView_write;

    private int begin;
    private String password=null;
    private String write_data=null;
    static String tag;
    public static WriteTagFragment newInstance(String rfid) {
        WriteTagFragment fragment = new WriteTagFragment();
        tag = rfid;
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_write_tag, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //先开启认证
        MyApplication.sv_Main.DoIndentify(true);
    }

    @OnClick({R2.id.write_button})
    void onViewClick(View view) {
        int id = view.getId();
        if (id == R.id.write_button) {
//获得区域信息
            String quyu_data = quyu_write_input.getSelectedItem().toString();
            int quyuId = 3;
            if(quyu_data.equals("用户区")){
                quyuId = 3;
            }else if(quyu_data.equals("安全区")){
                quyuId = 2;
            }else if(quyu_data.equals("标签信息区")){
                quyuId = 0;
            }else if(quyu_data.equals("编码区")){
                quyuId = 1;
            }
            /**
             * 发送指令
             */
            try {
                String zhizhenchangdu_data = zhizhenchangdu_write_input.getText().toString();
                begin = Integer.parseInt(zhizhenchangdu_data);
                String pw= writeOder_input1.getText().toString();
                password  = pw;
                String wd= writing_input.getText().toString();
                write_data = wd;

                MyApplication.sv_Main .DoWriteTagInfo(tag, quyuId, begin, write_data, password);
                //sv_Write.DoWriteTagInfo("A09000000000C509", 3, 0, write_data, "0000");
            } catch (Exception e) {
                AlertDialog Al = new AlertDialog.Builder(
                        _mActivity)
                        .setMessage("指令有误，请修改填写内容！")// 内容
                        .create();
                Al.show();
            }
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }
    @Subscriber(tag = EventBusTags.WRITETAG)
    private void updateUser(Event event) {
        //位于栈顶才接收
        Log.e("WRITETAG:", "WRITETAG");
        ToastUtil.showToast((String)event.getData());
        try {
            JSONObject szCode=  new JSONObject((String)event.getData());
            textView_write.setText((String)event.getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initEventAndData() {

    }
}
