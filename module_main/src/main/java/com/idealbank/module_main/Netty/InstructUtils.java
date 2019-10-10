package com.idealbank.module_main.Netty;

import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.mvp.model.entity.UpLoad;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;

import static me.jessyan.armscomponent.commonsdk.utils.ToastUtil.showToast;

//指令
public class InstructUtils {



  public static boolean send(Message message){
       if (ChannelMap.getChannel("1") != null) {

           ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
           return true;
       } else {
           showToast("发送失败，未与本机连接");
       }
      return false;
  }


//发送  查询资产
    public static void send(AssetsBean assetsBean,String time ){
        if (ChannelMap.getChannel("1") != null) {
            List<AssetsBean> addData = new ArrayList<>();
            assetsBean.setTime(System.currentTimeMillis());
            addData.add(assetsBean);

            Message message = new Message();
            message.setId(1);
            message.setType(MsgType.RFID);
            UpLoad upLoad = new UpLoad();
            upLoad.setId("1");
            upLoad.setDeviceId("");
            upLoad.setCreateTime(time);
            upLoad.setAssetList(addData);
            message.setResponseMessage(upLoad);
            ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
        } else {
            showToast("发送失败，未与本机连接");
        }


    }
}
