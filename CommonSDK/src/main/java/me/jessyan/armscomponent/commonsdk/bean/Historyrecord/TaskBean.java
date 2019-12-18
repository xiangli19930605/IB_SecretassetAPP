package me.jessyan.armscomponent.commonsdk.bean.Historyrecord;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class TaskBean extends  SelectIlem implements Serializable {

    private static final long serialVersionUID = 5298086812218887585L;
    //ID主键自增
    @Id(autoincrement = true)
    private Long id;

    private String  taskid;//任务id

    private int   state;//0：已上传 1：未上传
    private String      createTime;   //创建时间
    private String     finishTime;   //创建时间
    private int      number;   //件数
    private int passFlag;   //0允许，1拒绝
    private String reason;   //理由

    @Generated(hash = 1818773099)
    public TaskBean(Long id, String taskid, int state, String createTime,
            String finishTime, int number, int passFlag, String reason) {
        this.id = id;
        this.taskid = taskid;
        this.state = state;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.number = number;
        this.passFlag = passFlag;
        this.reason = reason;
    }
    @Generated(hash = 1443476586)
    public TaskBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTaskid() {
        return this.taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getFinishTime() {
        return this.finishTime;
    }
    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int getPassFlag() {
        return this.passFlag;
    }
    public void setPassFlag(int passFlag) {
        this.passFlag = passFlag;
    }
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
   



}
