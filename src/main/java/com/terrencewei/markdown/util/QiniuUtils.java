package com.terrencewei.markdown.util;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.terrencewei.markdown.bean.OSSConfig;

/**
 * Created by terrencewei on 2017/04/17.
 */
@Component
public class QiniuUtils {

    @Autowired
    private OSSConfig              mOSSConfig;

    private static final StringMap PUT_POLICY = new StringMap().put("returnBody",
            "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fileSize\":$(fsize)}");



    private String generateUploadToken(String bucket, String accessKey, String secretKey, String objectKey) {
        return Auth.create(accessKey, secretKey).uploadToken(bucket, objectKey, mOSSConfig.getExpireSeconds(),
                PUT_POLICY);
    }



    /**
     * for private resource
     * 
     * @param filePath
     * @return
     */
    public String downloadFileFromCloud(String filePath) {
        return Auth.create(mOSSConfig.getAccessKey(), mOSSConfig.getSecureKey())
                .privateDownloadUrl("http://ooj9mze8t.bkt.clouddn.com/" + filePath);
    }



    public QiniuPutResult uploadFile2Cloud(Object inputData, String objectKey) {
        // 构造一个带指定Zone对象的配置类
        /**
         * 其中关于Zone对象和机房的关系如下： 华东 Zone.zone0() 华北 Zone.zone1() 华南 Zone.zone2()
         * 北美 Zone.zoneNa0()
         */
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        // 如果是Windows情况下，格式是 D:\\qiniu\\test.png
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = objectKey;
        String upToken = generateUploadToken(mOSSConfig.getBucketName(), mOSSConfig.getAccessKey(),
                mOSSConfig.getSecureKey(), objectKey);
        try {
            Response response = null;
            if (inputData instanceof String) {
                response = uploadManager.put((String) inputData, key, upToken);
            } else if (inputData instanceof File) {
                response = uploadManager.put((File) inputData, key, upToken);
            } else if (inputData instanceof byte[]) {
                response = uploadManager.put((byte[]) inputData, key, upToken);
            } else if (inputData instanceof InputStream) {
                response = uploadManager.put((InputStream) inputData, key, upToken, null, null);
            } else {
                System.err.println("Invalid input date type:" + inputData);
            }
            // 解析上传成功的结果
            return response != null ? response.jsonToObject(QiniuPutResult.class) : null;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
        return null;
    }

    public class QiniuPutResult {
        public String key;
        public String hash;
        public String bucket;
        public long   fileSize;



        @Override
        public String toString() {
            return "QiniuPutResult [" + "key=" + key + ", hash=" + hash + ", bucket=" + bucket + ", fileSize="
                    + fileSize + ']';
        }
    }

}
