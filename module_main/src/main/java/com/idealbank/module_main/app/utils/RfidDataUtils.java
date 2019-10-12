package com.idealbank.module_main.app.utils;

import android.content.Context;

import com.idealbank.module_main.app.DbManager;

import java.util.List;

import me.jessyan.armscomponent.commonres.dialog.AppDialog;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.utils.CommonUtils;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;

public class RfidDataUtils {


    //处理离线
    public static Boolean changeOffline(Context content, List<AssetsBean> list, String taskid) {
        //先判断是否选择过离线模式   (两种情况  1 socket 2 )
//        if (!UsbUtils.getUsbType() && !new DbManager().getUsbState()) {
        if (!CommonUtils.isNetworkConnected()) {
            new AppDialog(content)
                    .setTitle("提示")
                    .setContent("链接断开，将进行离线模式")
                    .setSingleButton(new AppDialog.OnButtonClickListener() {
                        @Override
                        public void onClick(String val) {
                            new DbManager().setUsbState(true);
                            for (int i = 0; i < list.size(); i++) {
                                //数据库查询   rfid  离线数据
                                OffLineAssetsBean offLineAssetsBean = new DbManager().queryOffLineAssetsBeanWhereRfid(list.get(i).getRfidId());
                                AssetsBean assetsBean = new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskid, list.get(i).getRfidId());

                                //判断数据库是否有这个数据，有则判断时间，没有则直接不授权
                                if (offLineAssetsBean == null) {
                                    assetsBean.setPermissionState(3);
                                } else {
                                    //根据时间判断  逾期 设置1 未逾期0
                                    assetsBean.setPermissionState( DateUtils.compartoNow(offLineAssetsBean.getEndTime())?1:0);
                                    assetsBean.setPermissionState(offLineAssetsBean.getPermissionState());
                                    assetsBean.setAssetUser(offLineAssetsBean.getAssetUser());
                                    assetsBean.setBelongDept(offLineAssetsBean.getBelongDept());
                                    assetsBean.setLastApproveUser(offLineAssetsBean.getLastApproveUser());
                                    assetsBean.setId(offLineAssetsBean.getId());
                                    assetsBean.setOutBillId(offLineAssetsBean.getOutBillId());
                                    assetsBean.setAssetId(offLineAssetsBean.getAssetId());
                                    assetsBean.setAssetState(offLineAssetsBean.getAssetState());
                                    assetsBean.setEndTime(offLineAssetsBean.getEndTime());
                                    assetsBean.setAssetName(offLineAssetsBean.getAssetName());
                                    assetsBean.setTypeId(offLineAssetsBean.getTypeId());
                                    assetsBean.setAssetBrand(offLineAssetsBean.getAssetBrand());
                                    assetsBean.setAssetModel(offLineAssetsBean.getAssetModel());
                                }
                                new DbManager().upAssetsBeanWhereId(assetsBean);
                            }
                        }
                    })
                    .show();
            return true;
        } else {

        }
        return false;
    }

    //处理离线
    public static void changeOffline(List<AssetsBean> list,String taskid) {
        for (int i = 0; i < list.size(); i++) {
            //数据库查询   rfid  离线数据
            OffLineAssetsBean offLineAssetsBean = new DbManager().queryOffLineAssetsBeanWhereRfid(list.get(i).getRfidId());
            AssetsBean assetsBean = new DbManager().queryAssetsBeanWhereTaskidAndRfid(taskid, list.get(i).getRfidId());

            //判断数据库是否有这个数据，有则判断时间，没有则直接不授权
            if (offLineAssetsBean == null) {
                assetsBean.setPermissionState(3);
            } else {
                //根据时间判断  逾期 设置1 未逾期0
                assetsBean.setPermissionState(DateUtils.compartoNow(offLineAssetsBean.getEndTime()) ? 1 : 0);
                assetsBean.setAssetUser(offLineAssetsBean.getAssetUser());
                assetsBean.setBelongDept(offLineAssetsBean.getBelongDept());
                assetsBean.setLastApproveUser(offLineAssetsBean.getLastApproveUser());
                assetsBean.setId(offLineAssetsBean.getId());
                assetsBean.setOutBillId(offLineAssetsBean.getOutBillId());
                assetsBean.setAssetId(offLineAssetsBean.getAssetId());
                assetsBean.setAssetState(offLineAssetsBean.getAssetState());
                assetsBean.setEndTime(offLineAssetsBean.getEndTime());
                assetsBean.setAssetName(offLineAssetsBean.getAssetName());
                assetsBean.setTypeId(offLineAssetsBean.getTypeId());
                assetsBean.setAssetBrand(offLineAssetsBean.getAssetBrand());
                assetsBean.setAssetModel(offLineAssetsBean.getAssetModel());
            }
            new DbManager().upAssetsBeanWhereId(assetsBean);
        }

    }


}
