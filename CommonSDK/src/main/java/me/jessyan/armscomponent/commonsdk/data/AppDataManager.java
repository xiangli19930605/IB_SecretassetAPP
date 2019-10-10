package me.jessyan.armscomponent.commonsdk.data;



import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.data.db.DataManager;
import me.jessyan.armscomponent.commonsdk.data.db.DbHelper;
import me.jessyan.armscomponent.commonsdk.data.http.HttpHelper;
import me.jessyan.armscomponent.commonsdk.data.prefs.PreferenceHelper;

/**
 * @author flymegoc
 * @date 2017/11/22
 * @describe
 */

@Singleton
public class AppDataManager implements DataManager {

    private DbHelper mDbHelper;
    private HttpHelper mHttpHelper;
    private PreferenceHelper mPreferencesHelper;

    @Inject
    public AppDataManager(DbHelper mDbHelper, PreferenceHelper mPreferencesHelper, HttpHelper mHttpHelper) {
        this.mDbHelper = mDbHelper;
        this.mHttpHelper = mHttpHelper;
        this.mPreferencesHelper = mPreferencesHelper;
    }


    @Override
    public long insertTaskBean(TaskBean data) {
        return mDbHelper.insertTaskBean(data);
    }

    @Override
    public void clearTaskBean() {
        mDbHelper.clearTaskBean();
    }

    @Override
    public void delTaskBeanWhereId(Long id) {
        mDbHelper.delTaskBeanWhereId(id);
    }

    @Override
    public List<TaskBean> loadAllTaskBean() {
        return  mDbHelper.loadAllTaskBean();
    }

    @Override
    public List<TaskBean> queryTaskBeanWhereState(int state) {
        return mDbHelper.queryTaskBeanWhereState(state);
    }

    @Override
    public void upDateTaskBeanWhereId(Long data, int state) {
         mDbHelper.upDateTaskBeanWhereId(data,state);
    }

    @Override
    public void upDateNumberTaskBeanWhereId(String data, int number) {
        mDbHelper.upDateNumberTaskBeanWhereId(data,number);
    }

    @Override
    public long insertAssetsBean(AssetsBean data) {
        return  mDbHelper.insertAssetsBean(data);
    }

    @Override
    public void upAssetsBeanWhereId(AssetsBean data) {
        mDbHelper.upAssetsBeanWhereId(data);
    }

    @Override
    public void upAssetsBeanWhereId(List<AssetsBean> list) {
        mDbHelper.upAssetsBeanWhereId(list);
    }

    @Override
    public void insertInTxAssetsBean(List<AssetsBean> list) {
        mDbHelper.insertInTxAssetsBean(list);
    }

    @Override
    public List<AssetsBean> queryAssetsBeanWhereTaskid(String taskid) {
        return  mDbHelper.queryAssetsBeanWhereTaskid(taskid);
    }

    @Override
    public AssetsBean queryAssetsBeanWhereTaskidAndRfid(String taskid, String rfid) {
        return  mDbHelper.queryAssetsBeanWhereTaskidAndRfid(taskid,rfid);
    }

    @Override
    public void delAssetsBeanWhereId(Long id) {
        mDbHelper.delAssetsBeanWhereId(id);
    }

    @Override
    public void delAssetsBeanWhereTaskId(String taskid) {
        mDbHelper.delAssetsBeanWhereTaskId(taskid);
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        return mDbHelper.addHistoryData(data);
    }

    @Override
    public void clearHistoryData() {
         mDbHelper.clearHistoryData();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return mDbHelper.loadAllHistoryData();
    }

    @Override
    public long insertOffLineAssetsBean(OffLineAssetsBean data) {
        return mDbHelper.insertOffLineAssetsBean(data);
    }

    @Override
    public void insertInTxOffLineAssetsBean(List<OffLineAssetsBean> list) {
        mDbHelper.insertInTxOffLineAssetsBean(list);
    }

    @Override
    public void clearOffLineAssetsBean() {
        mDbHelper.clearOffLineAssetsBean();
    }

    @Override
    public List<OffLineAssetsBean> loadAllOffLineAssetsBean() {
        return mDbHelper.loadAllOffLineAssetsBean();
    }

    @Override
    public OffLineAssetsBean queryOffLineAssetsBeanWhereRfid(String rfid) {
        return mDbHelper.queryOffLineAssetsBeanWhereRfid(rfid);
    }


    @Override
    public boolean getNightModeState() {
        return  mPreferencesHelper.getNightModeState();
    }

    @Override
    public void setNightModeState(boolean b) {
        mPreferencesHelper .setNightModeState(b);
    }

    @Override
    public void setUsbState(boolean b) {
          mPreferencesHelper.setUsbState(b);
    }

    @Override
    public boolean getUsbState() {
        return mPreferencesHelper.getUsbState();
    }

    @Override
    public Observable<String> testPigAvAddress(String url) {
        return null;
    }
}
