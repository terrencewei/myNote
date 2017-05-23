package com.terrencewei.markdown.bean;

import javax.persistence.*;

/**
 * Created by terrencewei on 2017/04/28.
 */
@Entity
public class OSSObject {

    @Id
    @GeneratedValue
    private Long   id;
    private String objKey;
    @Lob
    @Column(length = 1048576) // max size is 1MB
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private String url;
    private String bucketName;
    private String hash;
    private long   size;
    private long   createdTime;
    private long   updateTime;



    public OSSObject() {
    }



    public OSSObject(String pObjKey, long pSize) {
        objKey = pObjKey;
        size = pSize;
    }



    public String getObjKey() {
        return objKey;
    }



    public void setObjKey(String pObjKey) {
        objKey = pObjKey;
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
        return "OSSObject [" + "id=" + id + ", objKey=" + objKey + ", content=" + content + ", url=" + url
                + ", bucketName=" + bucketName + ", hash=" + hash + ", size=" + size + ", createdTime=" + createdTime
                + ", updateTime=" + updateTime + ']';
    }
}
