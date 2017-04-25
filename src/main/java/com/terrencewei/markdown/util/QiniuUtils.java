package com.terrencewei.markdown.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
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



    public String downloadFileFromCloud(String objKey) {
        return Auth.create(mOSSConfig.getAccessKey(), mOSSConfig.getSecureKey())
                .privateDownloadUrl(mOSSConfig.getDownloadBaseUrl() + "/" + objKey);
    }



    public QiniuPutResult uploadFile2Cloud(Object inputData, String objectKey) {
        UploadManager uploadManager = generateUploadManager();
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



    public QiniuGetResult getFileFromCloud(String objKey) {

        QiniuGetResult result = new QiniuGetResult();

        try {
            FileInfo fileInfo = generateBucketManager().stat(mOSSConfig.getBucketName(), objKey);
            result.hash = fileInfo.hash;
            result.fileSize = fileInfo.fsize;
            result.mimeType = fileInfo.mimeType;
            result.putTime = fileInfo.putTime;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return result;
    }



    public QiniuGetAllResult getAllFilesFromCloud() {

        QiniuGetAllResult results = new QiniuGetAllResult();
        results.results = new ArrayList<QiniuGetResult>();

        // 文件名前缀
        String prefix = "";
        // 每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        // 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        // 列举空间文件列表
        BucketManager.FileListIterator fileListIterator = generateBucketManager()
                .createFileListIterator(mOSSConfig.getBucketName(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            // 处理获取的file list结果
            FileInfo[] items = fileListIterator.next();

            for (FileInfo item : items) {
                QiniuGetResult result = new QiniuGetResult();
                result.key = item.key;
                result.hash = item.hash;
                result.fileSize = item.fsize;
                result.mimeType = item.mimeType;
                result.putTime = item.putTime;
                result.endUser = item.endUser;

                results.results.add(result);
            }
        }
        return results;
    }



    private Configuration generateConfiguration() {
        // 构造一个带指定Zone对象的配置类
        /**
         * 其中关于Zone对象和机房的关系如下： 华东 Zone.zone0() 华北 Zone.zone1() 华南 Zone.zone2()
         * 北美 Zone.zoneNa0()
         */
        return new Configuration(Zone.zone2());
    }



    private Auth generateAuth(String accessKey, String secretKey) {
        return Auth.create(accessKey, secretKey);
    }



    private String generateUploadToken(String bucket, String accessKey, String secretKey, String objectKey) {
        return generateAuth(accessKey, secretKey).uploadToken(bucket, objectKey, mOSSConfig.getExpireSeconds(),
                PUT_POLICY);
    }



    private BucketManager generateBucketManager() {
        return new BucketManager(generateAuth(mOSSConfig.getAccessKey(), mOSSConfig.getSecureKey()),
                generateConfiguration());
    }



    private UploadManager generateUploadManager() {
        return new UploadManager(generateConfiguration());
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

    public class QiniuGetResult {
        public String key;
        public String hash;
        public long   fileSize;
        public long   putTime;
        public String mimeType;
        public String endUser;



        @Override
        public String toString() {
            return "QiniuGetResult [" + "key=" + key + ", hash=" + hash + ", fileSize=" + fileSize + ", putTime="
                    + putTime + ", mimeType=" + mimeType + ", endUser=" + endUser + ']';
        }
    }

    public class QiniuGetAllResult {
        public List<QiniuGetResult> results;



        @Override
        public String toString() {
            return "QiniuGetAllResult [" + "results=" + results + ']';
        }
    }

}
