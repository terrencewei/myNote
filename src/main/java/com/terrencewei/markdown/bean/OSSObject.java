package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSObject {

    String key;
    Object data;



    public String getKey() {
        return key;
    }



    public void setKey(String pKey) {
        key = pKey;
    }



    @Override
    public String toString() {
        return "OSSObject [" + "key=" + key + ", data=" + data + ']';
    }



    public Object getData() {
        return data;
    }



    public void setData(Object pData) {
        data = pData;
    }
}
