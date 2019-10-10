package me.jessyan.armscomponent.commonsdk.bean.Historyrecord;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class AssetsBean  extends SelectItem implements Serializable{

    private static final long serialVersionUID = 7241014571325945746L;
    //ID主键自增
    @Id(autoincrement = true)
    private Long uid;
    private Long assetIncId;  //资产
    private String rfidId;
    private String taskid;
    private int tagType;//资产编号类型
    private String assetUser;
    private String belongDept;
    private String lastApproveUser;
    private String id;  //授权信息
    private String outBillId;
    private String assetId;
    private String endTime;
    private String assetName;
    private String typeId;
    private String assetBrand;
    private String assetModel;

    private int permissionState=4;  //0 已授权     1 未授权 2     3      4查询中
    private int assetState=0;    //0   待返回 1  已返回   2不需要返回

    @Generated(hash = 1966816812)
    public AssetsBean(Long uid, Long assetIncId, String rfidId, String taskid,
            int tagType, String assetUser, String belongDept,
            String lastApproveUser, String id, String outBillId, String assetId,
            String endTime, String assetName, String typeId, String assetBrand,
            String assetModel, int permissionState, int assetState) {
        this.uid = uid;
        this.assetIncId = assetIncId;
        this.rfidId = rfidId;
        this.taskid = taskid;
        this.tagType = tagType;
        this.assetUser = assetUser;
        this.belongDept = belongDept;
        this.lastApproveUser = lastApproveUser;
        this.id = id;
        this.outBillId = outBillId;
        this.assetId = assetId;
        this.endTime = endTime;
        this.assetName = assetName;
        this.typeId = typeId;
        this.assetBrand = assetBrand;
        this.assetModel = assetModel;
        this.permissionState = permissionState;
        this.assetState = assetState;
    }
    @Generated(hash = 901117572)
    public AssetsBean() {
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
    public String getLastApproveUser() {
        return this.lastApproveUser;
    }
    public void setLastApproveUser(String lastApproveUser) {
        this.lastApproveUser = lastApproveUser;
    }
    public String getTaskid() {
        return this.taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
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
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
    public int getPermissionState() {
        return this.permissionState;
    }
    public void setPermissionState(int permissionState) {
        this.permissionState = permissionState;
    }
    public int getAssetState() {
        return this.assetState;
    }
    public void setAssetState(int assetState) {
        this.assetState = assetState;
    }
    public Long getAssetIncId() {
        return this.assetIncId;
    }
    public void setAssetIncId(Long assetIncId) {
        this.assetIncId = assetIncId;
    }
    public int getTagType() {
        return this.tagType;
    }
    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public boolean getSelect() {
        return select;
    }

    @Override
    public void setSelect(boolean tagType) {
        this.select = tagType;
    }

    @Override
    public long getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
    }
}
