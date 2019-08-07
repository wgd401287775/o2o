package com.o2o.dto;

public class OResult<T> {
    private boolean success;
    private T data;
    private String errorMsg;
    private int errorCode;

    public OResult() {

    }

    // 成功时的构造器
    public OResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // 失败时的构造器
    public OResult(boolean success, int errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
