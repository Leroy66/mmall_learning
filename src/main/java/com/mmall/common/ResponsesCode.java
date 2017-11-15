package com.mmall.common;

public enum ResponsesCode {

    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_AGUMENT(2, "ILLEGAL_ARGUMENT");


    private int code;
    private String desc;

    ResponsesCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
