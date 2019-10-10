package me.jessyan.armscomponent.commonsdk.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseBean<T> implements Serializable {
    private int state;
    private String message;
    private T data;

    /**
     * state : 1
     * message : ok
     * data : [{"spaceName":"一号楼资产查验终端","id":"11","equipmentId":"201908CHK02","parentId":"100","parents":",100"},{"spaceName":"四号楼资产查验终端","id":"14","equipmentId":"201908CHK03","parentId":"100","parents":",100"},{"spaceName":"5号楼资产查验终端","id":"16","equipmentId":"201908CHK04","parentId":"100","parents":",100"},{"spaceName":"资产查验终端(院门口)","id":"15","equipmentId":"201908CHK01","parentId":"100","parents":",100"}]
     */

}
