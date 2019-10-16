package com.idealbank.module_main.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import me.jessyan.armscomponent.commonsdk.bean.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusTags;
import me.jessyan.armscomponent.commonsdk.utils.EventBusUtils;

public class NetWorkReceiver extends BroadcastReceiver {
    public String NET_ETHERNET ="ETHERNET";
    public String NET_NOCONNECT = "NOCONNECT";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    int type2 = networkInfo.getType();
                    String typeName = networkInfo.getTypeName();
                    switch (type2) {
                        case 0://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                            System.out.println("-----------networktest---------移动 网络");
                            break;
                        case 1: //wifi网络
                            System.out.println("-----------networktest---------wifi网络");
                            break;
                        case 9:  //网线连接
                            System.out.println("-----------networktest---------网线连接");
                            EventBusUtils.sendEvent(new Event(NET_ETHERNET),EventBusTags.NETWORKCHANGE);
                            break;
                    }
                } else {// 无网络
                    System.out.println("-----------networktest---------无网络");
                    EventBusUtils.sendEvent(new Event(NET_NOCONNECT),EventBusTags.NETWORKCHANGE);
                }
            }
    }

}
