package com.terrencewei.markdown.bean;

/**
 * Created by terrencewei on 2017/04/21.
 */
public class OSSInput {

    private String      bucketName;
    private OSSObject[] objects;



    public String getBucketName() {
        return bucketName;
    }



    public void setBucketName(String pBucketName) {
        bucketName = pBucketName;
    }



    public OSSObject[] getObjects() {
        return objects;
    }



    public void setObjects(OSSObject[] pObjects) {
        objects = pObjects;
    }



    @Override
    public String toString() {
        return "OSSInput [" + "bucketName=" + bucketName + ", objects=" + objects + ']';
    }
}
