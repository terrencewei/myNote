package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSResponse {

    private boolean success;
    private String  code;
    private String  msg;
    private Object  data;



    public boolean isSuccess() {
        return success;
    }



    public void setSuccess(boolean pSuccess) {
        success = pSuccess;
    }



    public String getCode() {
        return code;
    }



    public void setCode(String pCode) {
        code = pCode;
    }



    public String getMsg() {
        return msg;
    }



    public void setMsg(String pMsg) {
        msg = pMsg;
    }



    public Object getData() {
        return data;
    }



    public void setData(Object pData) {
        data = pData;
    }



    @Override
    public String toString() {
        return "OSSResponse [" + "success=" + success + ", code=" + code + ", msg=" + msg + ", data=" + data + ']';
    }
}
