package com.terrencewei.markdown.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by terrencewei on 2017/04/21.
 */
@Component
@ConfigurationProperties(prefix = "ossConfig")
public class OSSConfig {

    private String accessKey;
    private String secureKey;
    private String bucketName;
    private int    expireSeconds;
    private String downloadBaseUrl;



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



    public String getDownloadBaseUrl() {
        return downloadBaseUrl;
    }



    public void setDownloadBaseUrl(String pDownloadBaseUrl) {
        downloadBaseUrl = pDownloadBaseUrl;
    }



    @Override
    public String toString() {
        return "OSSConfig [" + "accessKey=" + accessKey + ", secureKey=" + secureKey + ", bucketName=" + bucketName
                + ", expireSeconds=" + expireSeconds + ", downloadBaseUrl=" + downloadBaseUrl + ']';
    }
}
