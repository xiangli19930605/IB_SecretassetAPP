package me.jessyan.armscomponent.commonsdk.data.db;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;
import me.jessyan.armscomponent.commonsdk.greendao.AssetsBeanDao;
import me.jessyan.armscomponent.commonsdk.greendao.DaoSession;
import me.jessyan.armscomponent.commonsdk.greendao.HistoryDataDao;
import me.jessyan.armscomponent.commonsdk.greendao.OffLineAssetsBeanDao;
import me.jessyan.armscomponent.commonsdk.greendao.TaskBeanDao;


/**
 * 对外隐藏操作数据库的实现细节
 *
 * @author quchao
 * @date 2017/11/27
 */
@Singleton
public class DbHelperImpl implements DbHelper {

    private static final int HISTORY_LIST_SIZE = 10;

    private DaoSession daoSession;
    private List<HistoryData> historyDataList;
    private String data;
    private HistoryData historyData;

    @Inject
    DbHelperImpl() {
        daoSession = MyApplication.getDaoSession();
    }

    @Override
    public long insertTaskBean(TaskBean data) {
        return getTaskBeanDao().insert(data);
    }

    @Override
    public void clearTaskBean() {
        getTaskBeanDao().deleteAll();
    }

    @Override
    public void delTaskBeanWhereId(Long id) {
        TaskBean taskBean = getTaskBeanDao().queryBuilder().where(TaskBeanDao.Properties.Id.eq(id)).unique();
        //删除操作
        getTaskBeanDao().delete(taskBean);
    }

    @Override
    public List<TaskBean> loadAllTaskBean() {
        return getTaskBeanDao().loadAll();
    }

    @Override
    public List<TaskBean> queryTaskBeanWhereState(int state) {
        return  getTaskBeanDao().queryBuilder().where(TaskBeanDao.Properties.State.eq(state)).list();
    }

    @Override
    public void upDateTaskBeanWhereId(Long data, int state) {
        TaskBean taskBean = getTaskBeanDao(). queryBuilder().where(TaskBeanDao.Properties.Id.eq(data)).unique();
        taskBean.setState(state);
        getTaskBeanDao().update(taskBean);
    }

    @Override
    public void upDateNumberTaskBeanWhereId(String data, int number) {
        //number==0，删除，否则更新
        TaskBean taskBean = getTaskBeanDao(). queryBuilder().where(TaskBeanDao.Properties.Taskid.eq(data)).unique();
        if(number>0){
            taskBean.setNumber(number);
            getTaskBeanDao().update(taskBean);
        }else{
            //删除操作
            getTaskBeanDao().delete(taskBean);
        }
    }

    @Override
    public long insertAssetsBean(AssetsBean data) {
         return  getAssetsBeanDao().insert(data);
    }

    @Override
    public void upAssetsBeanWhereId(AssetsBean data) {
          getAssetsBeanDao().update(data);
    }

    @Override
    public void upAssetsBeanWhereId(List<AssetsBean> list) {
          getAssetsBeanDao().updateInTx(list);
    }

    @Override
    public void insertInTxAssetsBean(List<AssetsBean> list) {
        getAssetsBeanDao(). insertInTx(list);
    }

    @Override
    public List<AssetsBean> queryAssetsBeanWhereTaskid(String taskid) {
        return  getAssetsBeanDao().queryBuilder().where(AssetsBeanDao.Properties.Taskid.eq(taskid)).list();
    }

    @Override
    public AssetsBean queryAssetsBeanWhereTaskidAndRfid(String taskid, String rfid) {
        return  getAssetsBeanDao().queryBuilder().where(AssetsBeanDao.Properties.Taskid.eq(taskid), AssetsBeanDao.Properties.RfidId.eq(rfid)).unique();
    }

    @Override
    public void delAssetsBeanWhereId(Long Uid) {
        AssetsBean assetsBean = getAssetsBeanDao().queryBuilder().where(AssetsBeanDao.Properties.Uid.eq(Uid)).unique();
        //删除操作
        getAssetsBeanDao().delete(assetsBean);
    }

    @Override
    public void delAssetsBeanWhereTaskId(String taskid) {
        List<AssetsBean> list = queryAssetsBeanWhereTaskid(taskid);
        getAssetsBeanDao().deleteInTx(list);
    }


    @Override
    public List<HistoryData> addHistoryData(String data) {
        this.data = data;
        getHistoryDataList();
        createHistoryData();
        if (historyDataForward()) {
            return historyDataList;
        }

        if (historyDataList.size() < HISTORY_LIST_SIZE) {
            getHistoryDataDao().insert(historyData);
        } else {
            historyDataList.remove(0);
            historyDataList.add(historyData);
            getHistoryDataDao().deleteAll();
            getHistoryDataDao().insertInTx(historyDataList);
        }
        return historyDataList;
    }

    @Override
    public void clearHistoryData() {
        daoSession.getHistoryDataDao().deleteAll();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return daoSession.getHistoryDataDao().loadAll();
    }

    @Override
    public long insertOffLineAssetsBean(OffLineAssetsBean data) {
         return  getOffLineAssetsBeanDao().insert(data);
    }

    @Override
    public void insertInTxOffLineAssetsBean(List<OffLineAssetsBean> list) {
          getOffLineAssetsBeanDao().insertInTx(list);
    }

    @Override
    public void clearOffLineAssetsBean() {
        getOffLineAssetsBeanDao().deleteAll();
    }

    @Override
    public List<OffLineAssetsBean> loadAllOffLineAssetsBean() {
        return  getOffLineAssetsBeanDao().loadAll();
    }

    @Override
    public OffLineAssetsBean queryOffLineAssetsBeanWhereRfid(String rfid) {
        return    getOffLineAssetsBeanDao().queryBuilder().where(OffLineAssetsBeanDao.Properties.RfidId.eq(rfid)).unique();
    }

    /**
     * 历史数据前移
     *
     * @return 返回true表示查询的数据已存在，只需将其前移到第一项历史记录，否则需要增加新的历史记录
     */
    private boolean historyDataForward() {
        //重复搜索时进行历史记录前移
        Iterator<HistoryData> iterator = historyDataList.iterator();
        //不要在foreach循环中进行元素的remove、add操作，使用Iterator模式
        while (iterator.hasNext()) {
            HistoryData historyData1 = iterator.next();
            if (historyData1.getData().equals(data)) {
                historyDataList.remove(historyData1);
                historyDataList.add(historyData);
                getHistoryDataDao().deleteAll();
                getHistoryDataDao().insertInTx(historyDataList);
                return true;
            }
        }
        return false;
    }

    private void getHistoryDataList() {
        historyDataList = getHistoryDataDao().loadAll();
    }

    private void createHistoryData() {
        historyData = new HistoryData();
        historyData.setDate(System.currentTimeMillis());
        historyData.setData(data);
    }

    private HistoryDataDao getHistoryDataDao() {
        return daoSession.getHistoryDataDao();
    }



    private AssetsBeanDao getAssetsBeanDao() {
        return daoSession.getAssetsBeanDao();
    }
    private TaskBeanDao getTaskBeanDao() {
        return daoSession.getTaskBeanDao();
    }
    private OffLineAssetsBeanDao getOffLineAssetsBeanDao() {
        return daoSession.getOffLineAssetsBeanDao();
    }

}
