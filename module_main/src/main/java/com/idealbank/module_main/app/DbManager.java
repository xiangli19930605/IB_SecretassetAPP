package com.idealbank.module_main.app;

import java.util.List;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.data.db.DbHelper;
import me.jessyan.armscomponent.commonsdk.data.prefs.PreferenceHelper;

public class DbManager implements DbHelper , PreferenceHelper {


    @Override
    public long insertTaskBean(TaskBean data) {
        return MyApplication.mComponent
                .getAppDataManager().insertTaskBean(data);
    }

    @Override
    public void clearTaskBean() {
        MyApplication.mComponent
                .getAppDataManager().clearTaskBean();
    }

    @Override
    public void delTaskBeanWhereId(Long id) {
        MyApplication.mComponent
                .getAppDataManager().delTaskBeanWhereId(id);
    }

    @Override
    public List<TaskBean> loadAllTaskBean() {
        return MyApplication.mComponent
                .getAppDataManager().loadAllTaskBean();
    }

    @Override
    public List<TaskBean> queryTaskBeanWhereState(int state) {
        return MyApplication.mComponent
                .getAppDataManager().queryTaskBeanWhereState(state);
    }

    @Override
    public void upDateTaskBeanWhereId(Long data, int state) {
        MyApplication.mComponent
                .getAppDataManager().upDateTaskBeanWhereId( data,  state);
    }

    @Override
    public void upDateNumberTaskBeanWhereId(String data, int number) {
        MyApplication.mComponent
                .getAppDataManager().upDateNumberTaskBeanWhereId( data,  number);
    }

    @Override
    public long insertAssetsBean(AssetsBean data) {
        return  MyApplication.mComponent
                .getAppDataManager().insertAssetsBean( data  );
    }

    @Override
    public void upAssetsBeanWhereId(AssetsBean data) {
        MyApplication.mComponent
                .getAppDataManager().upAssetsBeanWhereId( data  );
    }

    @Override
    public void upAssetsBeanWhereId(List<AssetsBean> list) {
        MyApplication.mComponent
                .getAppDataManager().upAssetsBeanWhereId( list  );
    }

    @Override
    public void insertInTxAssetsBean(List<AssetsBean> list) {
        MyApplication.mComponent
                .getAppDataManager().insertInTxAssetsBean( list);
    }

    @Override
    public List<AssetsBean> queryAssetsBeanWhereTaskid(String taskid) {
        return  MyApplication.mComponent
                .getAppDataManager().queryAssetsBeanWhereTaskid( taskid);
    }

    @Override
    public AssetsBean queryAssetsBeanWhereTaskidAndRfid(String taskid, String rfid) {
        return  MyApplication.mComponent
                .getAppDataManager().queryAssetsBeanWhereTaskidAndRfid( taskid,rfid);
    }

    @Override
    public void delAssetsBeanWhereId(Long id) {
        MyApplication.mComponent
                .getAppDataManager().delAssetsBeanWhereId( id);
    }

    @Override
    public void delAssetsBeanWhereTaskId(String taskid) {
        MyApplication.mComponent
                .getAppDataManager().delAssetsBeanWhereTaskId( taskid);
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        return  MyApplication.mComponent
                .getAppDataManager().addHistoryData(data);
    }

    @Override
    public void clearHistoryData() {
        MyApplication.mComponent
                .getAppDataManager().clearHistoryData();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return  MyApplication.mComponent
                .getAppDataManager().loadAllHistoryData();
    }

    @Override
    public long insertOffLineAssetsBean(OffLineAssetsBean data) {
        return  MyApplication.mComponent
                .getAppDataManager().insertOffLineAssetsBean(data);
    }

    @Override
    public void insertInTxOffLineAssetsBean(List<OffLineAssetsBean> list) {
        MyApplication.mComponent
                .getAppDataManager().insertInTxOffLineAssetsBean(list);
    }

    @Override
    public void clearOffLineAssetsBean() {
        MyApplication.mComponent
                .getAppDataManager().clearOffLineAssetsBean();
    }

    @Override
    public List<OffLineAssetsBean> loadAllOffLineAssetsBean() {
        return  MyApplication.mComponent
                .getAppDataManager().loadAllOffLineAssetsBean();
    }

    @Override
    public OffLineAssetsBean queryOffLineAssetsBeanWhereRfid(String rfid) {
        return  MyApplication.mComponent
                .getAppDataManager().queryOffLineAssetsBeanWhereRfid(rfid);
    }


    @Override
    public boolean getNightModeState() {
        return false;
    }

    @Override
    public void setNightModeState(boolean b) {

    }

    @Override
    public void setUsbState(boolean b) {
          MyApplication.mComponent
                .getAppDataManager().setUsbState(b);
    }

    @Override
    public boolean getUsbState() {
        return  MyApplication.mComponent
                .getAppDataManager().getUsbState();
    }
}
