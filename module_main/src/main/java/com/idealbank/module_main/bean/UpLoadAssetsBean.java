package com.idealbank.module_main.bean;

import com.idealbank.module_main.mvp.model.entity.UpLoad;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpLoadAssetsBean {

    private DataBean data;
    private int deviceId;

    @Getter
    @Setter
    public static class DataBean {
        private int id;
        private UpLoad responseMessage;

    }
}
