package com.terrencewei.markdown.bean;

import java.util.Arrays;

/**
 * Created by terrencewei on 2017/04/28.
 */
public class OSSObject {

    private String key;
    private String content;
    private String url;
    private String bucketName;
    public String  hash;
    public long    size;
    public long    createdTime;
    public long    updateTime;



    public String getKey() {
        return key;
    }



    public void setKey(String pKey) {
        key = pKey;
    }



    public String getContent() {
        return content;
    }



    public void setContent(String pContent) {
        content = pContent;
    }



    public String getUrl() {
        return url;
    }



    public void setUrl(String pUrl) {
        url = pUrl;
    }



    public String getBucketName() {
        return bucketName;
    }



    public void setBucketName(String pBucketName) {
        bucketName = pBucketName;
    }



    public String getHash() {
        return hash;
    }



    public void setHash(String pHash) {
        hash = pHash;
    }



    public long getSize() {
        return size;
    }



    public void setSize(long pSize) {
        size = pSize;
    }



    public long getCreatedTime() {
        return createdTime;
    }



    public void setCreatedTime(long pCreatedTime) {
        createdTime = pCreatedTime;
    }



    public long getUpdateTime() {
        return updateTime;
    }



    public void setUpdateTime(long pUpdateTime) {
        updateTime = pUpdateTime;
    }



    @Override
    public String toString() {
        return "OSSObject [" + "key=" + key + ", content=" + content + ", url=" + url + ", bucketName=" + bucketName
                + ", hash=" + hash + ", size=" + size + ", createdTime=" + createdTime + ", updateTime=" + updateTime
                + ']';
    }
}
