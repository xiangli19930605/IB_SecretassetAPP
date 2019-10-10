package me.jessyan.armscomponent.commonsdk.data.db;

import java.util.List;

import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.OffLineAssetsBean;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.TaskBean;

/**
 * @author quchao
 * @date 2017/11/27
 */

public interface DbHelper {

    // 增加盘查任务数据
    long insertTaskBean(TaskBean data);


    void clearTaskBean();

    //删除指定ID的任务
    void delTaskBeanWhereId(Long id);

    //
    List<TaskBean> loadAllTaskBean();


    //根据状态查询任务
    List<TaskBean> queryTaskBeanWhereState(int state);

    //更新任务状态
    void upDateTaskBeanWhereId(Long data, int state);
//
//    //更新任务的数量
    void upDateNumberTaskBeanWhereId(String data, int number);
//


    //增加单个资产
    long insertAssetsBean(AssetsBean data);

    //更新单个资产
    void upAssetsBeanWhereId(AssetsBean data);

    //更新多个资产
    void upAssetsBeanWhereId(List<AssetsBean> list);


    //增加多个资产
    void insertInTxAssetsBean(List<AssetsBean> list);

    //根据任务编号CheckId查询
    List<AssetsBean> queryAssetsBeanWhereTaskid(String taskid);

    //根据任务编号CheckId查询 rfid
    AssetsBean queryAssetsBeanWhereTaskidAndRfid(String taskid, String rfid);

    //
//    void clearAssetsBean();
//
//    List<AssetsBean> loadAllAssetsBean();
//
//
//
//    //删除指定ID下的资产
    void delAssetsBeanWhereId(Long id);
    //    //删除指定taskid下的资产
    void delAssetsBeanWhereTaskId(String taskid);

    /**
     * 增加历史数据
     */
    List<HistoryData> addHistoryData(String data);

    /**
     *
     */
    void clearHistoryData();

    /**
     *
     */
    List<HistoryData> loadAllHistoryData();


    //增加单个离线资产
    long insertOffLineAssetsBean(OffLineAssetsBean data);

    //增加多个离线资产
    void insertInTxOffLineAssetsBean(List<OffLineAssetsBean> list);

    //删除全部离线资产
    void clearOffLineAssetsBean();

    //查询全部离线资产
    List<OffLineAssetsBean> loadAllOffLineAssetsBean();

    //查询是否含有rfid的资产
    OffLineAssetsBean queryOffLineAssetsBeanWhereRfid(String rfid);

}
