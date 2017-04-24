package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSResponse {

    private boolean success;
    private String  repsonseCode;
    private String  responseMsg;
    private Object  responseData;



    public boolean isSuccess() {
        return success;
    }



    public void setSuccess(boolean pSuccess) {
        success = pSuccess;
    }



    public String getRepsonseCode() {
        return repsonseCode;
    }



    public void setRepsonseCode(String pRepsonseCode) {
        repsonseCode = pRepsonseCode;
    }



    public String getResponseMsg() {
        return responseMsg;
    }



    public void setResponseMsg(String pResponseMsg) {
        responseMsg = pResponseMsg;
    }



    public Object getResponseData() {
        return responseData;
    }



    public void setResponseData(Object pResponseData) {
        responseData = pResponseData;
    }



    @Override
    public String toString() {
        return "OSSResponse [" + "success=" + success + ", repsonseCode=" + repsonseCode + ", responseMsg="
                + responseMsg + ", responseData=" + responseData + ']';
    }
}
