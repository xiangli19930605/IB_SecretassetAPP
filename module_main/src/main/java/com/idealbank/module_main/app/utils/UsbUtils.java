package com.idealbank.module_main.app.utils;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

public class UsbUtils {

    public static boolean getUsbType() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = MyApplication.getApplication().registerReceiver(null, ifilter);
//如果设备正在充电，可以提取当前的充电状态和充电方式（无论是通过 USB 还是交流充电器），如下所示：

// Are we charging / charged?
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (isCharging) {
            return true;
//            if (usbCharge) {
//                Toast.makeText(MainActivity.this, "手机正处于USB连接！", Toast.LENGTH_SHORT).show();
//            } else if (acCharge) {
//                Toast.makeText(MainActivity.this, "手机通过电源充电中！", Toast.LENGTH_SHORT).show();
//            }
        } else {
            return false;
//            Toast.makeText(MainActivity.this, "手机未连接USB线！", Toast.LENGTH_SHORT).show();
        }

    }

}
