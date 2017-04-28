package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSConfig {

    private String accessKey;
    private String secureKey;
    private String bucketName;
    private String endPoint;
    private int    expireSeconds;



    public String getAccessKey() {
        return accessKey;
    }



    public void setAccessKey(String pAccessKey) {
        accessKey = pAccessKey;
    }



    public String getSecureKey() {
        return secureKey;
    }



    public void setSecureKey(String pSecureKey) {
        secureKey = pSecureKey;
    }



    public String getBucketName() {
        return bucketName;
    }



    public void setBucketName(String pBucketName) {
        bucketName = pBucketName;
    }



    public int getExpireSeconds() {
        return expireSeconds;
    }



    public void setExpireSeconds(int pExpireSeconds) {
        expireSeconds = pExpireSeconds;
    }



    public String getEndPoint() {
        return endPoint;
    }



    public void setEndPoint(String pEndPoint) {
        endPoint = pEndPoint;
    }



    @Override
    public String toString() {
        return "OSSConfig [" + "accessKey=" + accessKey + ", secureKey=" + secureKey + ", bucketName=" + bucketName
                + ", expireSeconds=" + expireSeconds + ", endPoint=" + endPoint + ']';
    }
}
