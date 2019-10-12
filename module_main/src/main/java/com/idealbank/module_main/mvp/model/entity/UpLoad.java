package com.idealbank.module_main.mvp.model.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;

@Getter
@Setter
public class UpLoad {
    /**
     * id : SW-0002
     * deviceId : ERWERW
     * passFlag : 1
     * reason : 未授权
     * assetList : [{"id":"SW-0002","rfidId":"TREQ","assetState":"1","assetUser":"22","belongDept":"017","permissionState":"1","lastApproveUser":"017"}]
     */

    private String taskId;
    private String id;
    private String createTime;
    private String deviceId;
    private int passFlag;   //0允许，1拒绝
    private String reason;   //理由
    private List<AssetsBean> assetList;

//    public UpLoad( ) {
//        this.id = "";
//        this.deviceId ="2";
//        this.passFlag = "";
//        this.reason = "";
//        this.assetList = new ArrayList<>();
//        this.assetList.add(new AssetsBean(Long.valueOf(1),"1001","1","冯","技术","2","2","10","","","","","","",0,0));
//
//    }





}
