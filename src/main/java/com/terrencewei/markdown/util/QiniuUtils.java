package com.terrencewei.markdown.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * Created by terrencewei on 2017/04/17.
 */
public class QiniuUtils {

    private static final String    AK              = "qJ0bpY5pwPjmTwQwVux531KB8Cx7Jf3i9YprI9am";
    private static final String    SK              = "NFnitaih6q-tHaTIpseULWN1qt48OU8oQKg5jZei";
    private static final String    BUCKET_NAME     = "terrenceweimarkdown";
    private static final int       EXPIRE_SECOUNDS = 3600;
    private static final StringMap PUT_POLICY      = new StringMap().put("returnBody",
            "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fileSize\":$(fsize)}");



    private static String generateUploadToken(String bucket, String accessKey, String secretKey, String objectKey) {
        return Auth.create(accessKey, secretKey).uploadToken(bucket, objectKey, EXPIRE_SECOUNDS, PUT_POLICY);
    }



    /**
     * for private resource
     * 
     * @param filePath
     * @return
     */
    public String downloadFileFromCloud(String filePath) {
        return Auth.create(AK, SK).privateDownloadUrl("http://ooj9mze8t.bkt.clouddn.com/" + filePath);
    }



    public QiniuPutResult uploadFile2Cloud(String filePath, String objectKey) {
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
        String upToken = generateUploadToken(BUCKET_NAME, AK, SK, objectKey);
        try {
            Response response = uploadManager.put(filePath, key, upToken);
            // 解析上传成功的结果
            return response.jsonToObject(QiniuPutResult.class);
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

    class QiniuPutResult {
        public String key;
        public String hash;
        public String bucket;
        public long   fileSize;
    }

}
