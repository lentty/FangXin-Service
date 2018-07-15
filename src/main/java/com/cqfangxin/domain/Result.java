package com.cqfangxin.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Result {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    // 操作结果
    private String resultCode;

    // 错误信息
    private String errorInfo;

    //错误代码
    private int status;

    // 附属对象
    private Object object;


    public Result(String resultCode, String errorInfo) {
        super();
        this.resultCode = resultCode;
        this.errorInfo = errorInfo;
    }

    public Result(String resultCode, String errorInfo, Object object) {
        this.resultCode = resultCode;
        this.errorInfo = errorInfo;
        this.object = object;
    }

    public Result(String resultCode, int status) {
        this.resultCode = resultCode;
        this.status = status;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
