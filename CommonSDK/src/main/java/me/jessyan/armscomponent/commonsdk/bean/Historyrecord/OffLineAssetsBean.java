package me.jessyan.armscomponent.commonsdk.bean.Historyrecord;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class OffLineAssetsBean implements Serializable {
//离线数据
    private static final long serialVersionUID = 4357755447610054857L;
    //ID主键自增
    @Id(autoincrement = true)
    private Long  uid;


    private String rfidId;

    private int assetState;
    private int permissionState;
    private String assetUser;
    private String belongDept;
    private String lastApproveUser;
    private String endTime;
    private String id;
    private String outBillId;
    private String assetId;
    private String assetName;
    private String typeId;
    private String assetBrand;
    private String assetModel;




    @Generated(hash = 784325669)
    public OffLineAssetsBean(Long uid, String rfidId, int assetState,
            int permissionState, String assetUser, String belongDept,
            String lastApproveUser, String endTime, String id, String outBillId,
            String assetId, String assetName, String typeId, String assetBrand,
            String assetModel) {
        this.uid = uid;
        this.rfidId = rfidId;
        this.assetState = assetState;
        this.permissionState = permissionState;
        this.assetUser = assetUser;
        this.belongDept = belongDept;
        this.lastApproveUser = lastApproveUser;
        this.endTime = endTime;
        this.id = id;
        this.outBillId = outBillId;
        this.assetId = assetId;
        this.assetName = assetName;
        this.typeId = typeId;
        this.assetBrand = assetBrand;
        this.assetModel = assetModel;
    }
    @Generated(hash = 39908642)
    public OffLineAssetsBean() {
    }
    public Long getUid() {
        return this.uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public String getRfidId() {
        return this.rfidId;
    }
    public void setRfidId(String rfidId) {
        this.rfidId = rfidId;
    }
    public int getAssetState() {
        return this.assetState;
    }
    public void setAssetState(int assetState) {
        this.assetState = assetState;
    }
    public String getAssetUser() {
        return this.assetUser;
    }
    public void setAssetUser(String assetUser) {
        this.assetUser = assetUser;
    }
    public String getBelongDept() {
        return this.belongDept;
    }
    public void setBelongDept(String belongDept) {
        this.belongDept = belongDept;
    }
    public int getPermissionState() {
        return this.permissionState;
    }
    public void setPermissionState(int permissionState) {
        this.permissionState = permissionState;
    }
    public String getLastApproveUser() {
        return this.lastApproveUser;
    }
    public void setLastApproveUser(String lastApproveUser) {
        this.lastApproveUser = lastApproveUser;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOutBillId() {
        return this.outBillId;
    }
    public void setOutBillId(String outBillId) {
        this.outBillId = outBillId;
    }
    public String getAssetId() {
        return this.assetId;
    }
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    public String getAssetName() {
        return this.assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public String getTypeId() {
        return this.typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getAssetBrand() {
        return this.assetBrand;
    }
    public void setAssetBrand(String assetBrand) {
        this.assetBrand = assetBrand;
    }
    public String getAssetModel() {
        return this.assetModel;
    }
    public void setAssetModel(String assetModel) {
        this.assetModel = assetModel;
    }











}
