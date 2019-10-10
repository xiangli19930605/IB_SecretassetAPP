package com.idealbank.module_main.Netty.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    private DataBean data;

    private MsgType type;

    @Getter
    @Setter
    public static class DataBean<T> {

        private int state;//失败0成功1
        private String message;
        private T data;

    }
}
