package com.terrencewei.markdown.model;

import javax.persistence.*;

/**
 * Created by terrencewei on 3/10/17.
 */
@Entity
public class OSSEntity {

    @Id
    @GeneratedValue
    private Long   id;
    private String key;
    @Lob
    @Column(length = 1048576) // max size is 1MB
    @Basic(fetch = FetchType.LAZY)
    private String content;
    private String url;
    private String bucketName;
    public String  hash;
    public Long    size;
    public Long    createdTime;
    public Long    updateTime;



    public Long getId() {
        return id;
    }



    public void setId(Long pId) {
        id = pId;
    }



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



    public Long getSize() {
        return size;
    }



    public void setSize(Long pSize) {
        size = pSize;
    }



    public Long getCreatedTime() {
        return createdTime;
    }



    public void setCreatedTime(Long pCreatedTime) {
        createdTime = pCreatedTime;
    }



    public Long getUpdateTime() {
        return updateTime;
    }



    public void setUpdateTime(Long pUpdateTime) {
        updateTime = pUpdateTime;
    }
}
