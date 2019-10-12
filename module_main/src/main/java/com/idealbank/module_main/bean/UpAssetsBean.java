package com.idealbank.module_main.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.AssetsBean;
//查询的资产
@Getter
@Setter
public class UpAssetsBean {

    private RfidIdBean rfidId;

    @Getter
    @Setter
    public static class RfidIdBean {
        private int id;
        private ResponseMessageBean responseMessage;

        @Getter
        @Setter
        public static class ResponseMessageBean {
            private String deviceId;
            private String passFlag;
            private String reason;
            private List<AssetsBean> assetList;
        }
    }
}
