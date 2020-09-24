package com.idealbank.module_main.Netty;

import com.idealbank.module_main.Netty.bean.Message;
import com.idealbank.module_main.Netty.bean.MsgType;
import com.idealbank.module_main.app.DbManager;
import com.idealbank.module_main.bean.Location;
import com.idealbank.module_main.bean.UpAssetsBean;
import com.idealbank.module_main.bean.UpLoadAssetsBean;
import com.idealbank.module_main.mvp.model.entity.UpLoad;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.constants.Constants;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.armscomponent.commonsdk.utils.GsonUtil;
import me.jessyan.autosize.utils.LogUtils;

import static me.jessyan.armscomponent.commonsdk.utils.ToastUtil.showToast;

//指令
public class InstructUtils {


    public static boolean send(Message message) {
        if (ChannelMap.getChannel("1") != null) {


            ChannelMap.getChannel("1").writeAndFlush(new TextWebSocketFrame(GsonUtil.GsonString(message)));
            return true;
        } else {
            showToast("发送失败，未与本机连接");
        }
        return false;
    }


    //发送  查询资产
    public static void send(AssetsBean assetsBean, String time) {
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

    //查询RFID的Message
    public static Message getRFIDMessage(List<AssetsBean> addData) {
        Message message = new Message();
        message.setId(1);
        message.setType(MsgType.RFID);
        UpLoad upLoad = new UpLoad();
        upLoad.setId("1");
        upLoad.setDeviceId("");
        upLoad.setAssetList(addData);
        message.setResponseMessage(upLoad);

        return message;
    }

    //上传记录的Message
    public static Message getUPLOADDATAMessage(String createTime, String reason, List<AssetsBean> mList) {
        Message message = new Message();
        message.setId(1);
        message.setType(MsgType.UPLOADDATA);
        UpLoad upLoad = new UpLoad();
        upLoad.setCreateTime(createTime);
        upLoad.setReason(reason);
        upLoad.setPassFlag(0);
        upLoad.setAssetList(mList);
        message.setResponseMessage(upLoad);

        return message;
    }


    //查询rfid时   获取类
    public static UpAssetsBean getUpAssetsBean(List<AssetsBean> addData) {
        Location location = GsonUtil.GsonToBean(new DbManager().getLocation(), Location.class);
        if (location == null) {
            return null;
        }
        UpAssetsBean upAssetsBean = new UpAssetsBean();
        UpAssetsBean.RfidIdBean.ResponseMessageBean responseMessageBean = new UpAssetsBean.RfidIdBean.ResponseMessageBean();
        responseMessageBean.setAssetList(addData);
        responseMessageBean.setDeviceId(location.getId());
        UpAssetsBean.RfidIdBean rfidIdBean = new UpAssetsBean.RfidIdBean();
        rfidIdBean.setId(1);
        rfidIdBean.setResponseMessage(responseMessageBean);
        upAssetsBean.setRfidId(rfidIdBean);
        return upAssetsBean;
    }

    //接口上传记录时   获取类
    public static UpLoadAssetsBean getUpLoadAssetsBean(String taskid, String createTime, String reason, List<AssetsBean> mList) {
            Location location = GsonUtil.GsonToBean(new DbManager().getLocation(), Location.class);
            if (location == null) {
                return null;
            }
            UpLoadAssetsBean upLoadAssetsBean = new UpLoadAssetsBean();
            upLoadAssetsBean.setDeviceId(Integer.valueOf(location.getId()));
            UpLoadAssetsBean.DataBean dataBean = new UpLoadAssetsBean.DataBean();
            dataBean.setId(1);
            UpLoad upLoad = new UpLoad();
            upLoad.setCreateTime(createTime);
            upLoad.setReason(reason);
            upLoad.setPassFlag(1);
            upLoad.setAssetList(mList);
            upLoad.setTaskId(taskid);
            dataBean.setResponseMessage(upLoad);
            upLoadAssetsBean.setData(dataBean);
            return upLoadAssetsBean;
    }


}
