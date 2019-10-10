package com.idealbank.module_main.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.barlibrary.ImmersionBar;
import com.idealbank.module_main.Netty.ChannelMap;
import com.idealbank.module_main.Netty.ImageUtils;
import com.idealbank.module_main.Netty.NettyClient;
import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.R;
import com.idealbank.module_main.R2;

import io.reactivex.functions.Consumer;
import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.jess.arms.di.component.AppComponent;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.jessyan.armscomponent.commonsdk.base.activity.ActionBarActivity;

import static me.jessyan.armscomponent.commonsdk.utils.ToastUtil.showToast;

public class TcpLinkActivity extends ActionBarActivity {
    private static final String TAG = "ServerThread";
    @BindView(R2.id.title)
    Button title;
    @BindView(R2.id.tv_state)
    TextView tv_state;
    @BindView(R2.id.received)
    TextView received;

    @BindView(R2.id.btn_send)
    Button btn_send;
    @BindView(R2.id.btn_clear)
    Button btn_clear;
    @BindView(R2.id.btn_sendExcl)
    Button btn_sendExcl;
    @BindView(R2.id.btn_sendPic)
    Button btn_sendPic;
    private Message message;


    @OnClick({R2.id.title, R2.id.btn_send, R2.id.btn_clear, R2.id.btn_sendPic,R2.id.btn_sendExcl,R2.id.btn_sendoffline,R2.id.btn_sendrfid})
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.btn_clear) {
            string = "";
            received.setText(string);

        } else if (i1 == R.id.title) {
//            if (ServiceUtils.isServiceRunning(this, "com.idealbank.module_main.app.service.TestOneService")) {
//                showToast("服务已开启");
//            } else {
//                title.setText("正在连接。。。。");
//                Intent intentFive = new Intent(this, TestOneService.class);
//                startService(intentFive);
//            }
            NettyClient.getInstance().connect().subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        showToast("已连接");
                    } else {
                        showToast("连接失败");
                    }
                }
            });





        } else if (i1 == R.id.btn_send) {
            if (ChannelMap.getChannel("1") != null) {
//                WebSocketHandler.channels.
                Message message=new Message();
//                message.setId(1);
//                message.setType(MsgType.PING);
//                message.setResponseMessage("响应内容" + "：这是PING");

                message.setId(1);
                message.setType(MsgType.RFID);
                ArrayList<AssetsBean> list=    new ArrayList<AssetsBean>();
                message.setResponseMessage(list);

                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
//                NettyClient.getInstance().send().subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            showToast("发送成功");
//                        } else {
//                            showToast("发送失败");
//                        }
//                    }
//                });

//                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));

            } else {
                    showToast("未连接");
            }

        } else if (i1 == R.id.btn_sendPic) {
            if (ChannelMap.getChannel("1") != null) {
//                for (int i = 0; i < 2; i++) {
                    Message message = new Message();
                    message.setId(1);
                    message.setType(MsgType.PIC);
                    message.setResponseMessage("响应内容" + "：这是PIC");
                    File file = new File("/storage/emulated/0/DCIM/Camera/pic.jpg");
                ImageUtils.getImg(file);
                    message.setAttachment(ImageUtils.getImg(file));
                    if (file.exists()) {

                        Log.e("file",""+file.length()+ImageUtils.getImg(file).length);
                    }
//                    ChannelMap.getChannel("1").writeAndFlush(JSON.toJSONString(message));
                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
//                }
            } else {
                showToast("未连接");
            }

        } else if (i1 == R.id.btn_sendExcl) {
            if (ChannelMap.getChannel("1") != null) {
                Message message = new Message();
                message.setId(1);
                message.setType(MsgType.UPLOADDATA);
                message.setResponseMessage(new UpLoad());
                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
            } else {
                showToast("未连接");
            }

        }else if (i1 == R.id.btn_sendrfid) {
            if (ChannelMap.getChannel("1") != null) {
                Message message = new Message();
                message.setId(1);
                message.setType(MsgType.RFID);
                message.setResponseMessage(new UpLoad());
                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
            } else {
                showToast("未连接");
            }
        }
        else if (i1 == R.id.btn_sendoffline) {
            if (ChannelMap.getChannel("1") != null) {
                Message message = new Message();
                message.setId(1);
                message.setType(MsgType.OFFLINE);
//                message.setResponseMessage(new UpLoad());
                ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
            } else {
                showToast("未连接");
            }

        }

    }



    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_tcp_link;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
    }


    /**
     * 子类接受事件 重写该方法
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventBus(NetEvent event) {
//
//    }
    @Subscriber
    private void updateUser(Event event) {
//        Log.e("222", "### update user name = " + event.isOpenServer);
//        title.setText("已开启服务..."+ event.isOpenServer);

        received.setText((String)event.getAction());

    }


//    @Override
//    public void handleMsg(Message msg) {
//        String m = msg.obj + "";
//
//        switch (msg.what) {
//            case 0x00:
//                //online
//                title.setText("已开启服务...");
//                break;
//            case 0x01:
//
//                tv_state.setText(m);
//                break;
//            case 0x02:
//                string = string + m + "\n";
//                received.setText(string);
//                break;
//        }
//    }
    String string = "" + "\n";
}
