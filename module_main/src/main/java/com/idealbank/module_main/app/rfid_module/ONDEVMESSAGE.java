package com.idealbank.module_main.app.rfid_module;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.autosize.utils.LogUtils;

public class ONDEVMESSAGE implements rfid_interface.OnReadMessage {

    public List<String> list = new ArrayList<String>();
    private JSONObject json_data;

    @Override
    public void OnReadMessage(String szCode) {
        LogUtils.e(szCode + "size:" + list.size());
        try {
            json_data = new JSONObject(szCode);
            String action = json_data.getString("action");
            if (action.equals("inventory")) {
                String originTagid = json_data.getString("tagid");//初始的ID
                String tagid;
                String type;//类型 fe:院内资产 ff:车辆标签 fc:院外资产 研究院内涉密资产标签 = "fe";研究院内普通资产标签= "fd"; 外来物品电子标签 = "fc"; 电子车标 "ff";
                int tagType;//类型  0123
                int str;//实际数据长度
                try {
                    type = originTagid.substring(0, 2);
                    //过滤掉其他标签  留下FE FC
                    if (type.equals("FE")) {
                        tagType = 0;
                    } else if (type.equals("FC")) {
                        tagType = 2;
                    } else {
                        return;
                    }
                    str = Integer.valueOf(originTagid.substring(2, 4));//截取前两个字符
                } catch (NumberFormatException e) {
                    LogUtils.e("" + e);
                    return;
                }
                try {
                    tagid = originTagid.substring(4, 4 + str);
                } catch (StringIndexOutOfBoundsException e) {
                    LogUtils.e("" + e);
                    return;
                }
                EventBusUtils.sendEvent(new Event(new RfidData(tagid, tagType)), EventBusTags.SCANRFID);
            } else if (action.equals("readtag")) {
                // 读取标签
                // {"action":"readtag", "error":"0", "tagid":"A09000000000C509", "data":"0000000000000000"}
                EventBusUtils.sendEvent(new Event("", szCode), EventBusTags.READTAG);
            } else if (action.equals("writetag")) {
                EventBusUtils.sendEvent(new Event("", szCode), EventBusTags.WRITETAG);
                json_data.getString("error");
                if (json_data.getString("error").equals("0")) {
                    System.out.println("写入成功");
                    ToastUtil.showToast("写入成功");
                } else {
                    System.out.println(json_data.getString("error"));
                    ToastUtil.showToast(json_data.getString("error"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
