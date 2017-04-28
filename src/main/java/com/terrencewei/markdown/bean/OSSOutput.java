package com.terrencewei.markdown.bean;

import java.util.Arrays;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSOutput {

    private boolean     success;
    private String      code;
    private String      msg;
    private OSSObject[] objects;



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



    public OSSObject[] getObjects() {
        return objects;
    }



    public void setObjects(OSSObject[] pObjects) {
        objects = pObjects;
    }



    @Override
    public String toString() {
        return "OSSOutput [" + "success=" + success + ", code=" + code + ", msg=" + msg + ", objects="
                + Arrays.toString(objects) + ']';
    }
}
