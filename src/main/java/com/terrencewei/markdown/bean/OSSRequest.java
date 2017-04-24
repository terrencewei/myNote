package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSRequest {

    private String objKey;
    private Object objData;



    public String getObjKey() {
        return objKey;
    }



    public void setObjKey(String pObjKey) {
        objKey = pObjKey;
    }



    public Object getObjData() {
        return objData;
    }



    public void setObjData(Object pObjData) {
        objData = pObjData;
    }



    @Override
    public String toString() {
        return "OSSRequest [" + "objKey=" + objKey + ", objData=" + objData + ']';
    }
}
