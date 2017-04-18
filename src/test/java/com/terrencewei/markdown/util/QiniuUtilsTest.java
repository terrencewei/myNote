package com.terrencewei.markdown.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by terrencewei on 2017/04/17.
 */
public class QiniuUtilsTest {

    private QiniuUtils mQiniuUtils = new QiniuUtils();



    @Test
    public void downloadFileFromCloud() throws Exception {
        String objectKey = "test.md";

        System.out.println("Download file key is:");
        System.out.println(objectKey);
        System.out.println("");
        System.out.println("Download url is:");
        System.out.println(mQiniuUtils.downloadFileFromCloud(objectKey));
    }



    @Test
    public void uploadFile2Cloud() throws Exception {

        String objectKey = "test.md";
        String uploadFilePath = "/home/terrencewei/Downloads/test.md";

        QiniuUtils.QiniuPutResult result = mQiniuUtils.uploadFile2Cloud(uploadFilePath, objectKey);

        System.out.println("upload result: ");
        System.out.println("-------------------------");
        System.out.println("key: " + result.key);
        System.out.println("hash: " + result.hash);
        System.out.println("bucket: " + result.bucket);
        long fileSise = (result.fileSize) / 1024;
        boolean showFileSizeAsKB = fileSise >= 1l;
        System.out.println("file size: " + (showFileSizeAsKB ? fileSise : result.fileSize)
                + (showFileSizeAsKB ? " KB" : " bytes"));
        System.out.println("");
        System.out.println("file download url: " + mQiniuUtils.downloadFileFromCloud(objectKey));
    }

}