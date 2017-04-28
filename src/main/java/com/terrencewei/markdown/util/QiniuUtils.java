package com.terrencewei.markdown.util;

import java.io.File;
import java.io.InputStream;

import com.terrencewei.markdown.bean.OSSConfig;
import com.terrencewei.markdown.bean.OSSConfigQiniu;
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
import com.terrencewei.markdown.bean.OSSObject;

/**
 * Created by terrencewei on 2017/04/17.
 *
 * 七牛云
 * 
 * https://developer.qiniu.com/kodo/sdk/1239/java
 */
@Component
public class QiniuUtils extends OSSUtilsImpl {

    private static final StringMap PUT_POLICY = new StringMap().put("returnBody",
            "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fileSize\":$(fsize)}");

    @Autowired
    protected OSSConfigQiniu       config;



    public QiniuUtils() {
        super.config = this.config;
    }



    @Override
    protected OSSObject putSingle(String pKey, String pContent) {
        OSSObject output = null;

        QiniuPutResult result = uploadFile2Cloud(pKey, pContent.getBytes());
        if (result != null) {
            output = new OSSObject();
            output.setKey(result.key);
            output.setHash(result.hash);
            output.setSize(result.fileSize);
            output.setBucketName(result.bucket);
        }
        return output;
    }



    @Override
    protected OSSObject getSingle(String pKey) {
        OSSObject output = null;
        QiniuGetResult result = getFileFromCloud(pKey);
        if (result != null) {
            output = new OSSObject();

            output.setKey(result.key);
            output.setSize(result.fileSize);
            output.setHash(result.hash);
            output.setCreatedTime(result.putTime);
            String downloadURL = downloadFileFromCloud(pKey);
            output.setUrl(downloadURL);
            output.setContent(getContentByURL(downloadURL));
        }
        return output;
    }



    @Override
    protected OSSObject[] listAll() {
        OSSObject[] outputs = null;
        QiniuGetAllResult results = getAllFilesFromCloud();
        if (results != null && results.results != null && results.results.length > 0) {
            outputs = new OSSObject[results.results.length];
            for (int i = 0; i < results.results.length; i++) {
                OSSObject obj = new OSSObject();
                QiniuGetResult result = results.results[i];
                obj.setKey(result.key);
                obj.setHash(result.hash);
                obj.setSize(result.fileSize);
                obj.setCreatedTime(result.putTime);

                outputs[i] = obj;
            }
        }
        return outputs;
    }



    private String downloadFileFromCloud(String objKey) {
        return Auth.create(config.getAccessKey(), config.getSecureKey())
                .privateDownloadUrl("http://" + config.getEndPoint() + "/" + objKey);
    }



    private QiniuPutResult uploadFile2Cloud(String objectKey, Object inputData) {
        UploadManager uploadManager = generateUploadManager();
        // 如果是Windows情况下，格式是 D:\\qiniu\\test.png
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = objectKey;
        String upToken = generateUploadToken(objectKey);
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



    private QiniuGetResult getFileFromCloud(String pKey) {

        QiniuGetResult result = new QiniuGetResult();

        try {
            FileInfo fileInfo = generateBucketManager().stat(config.getBucketName(), pKey);
            result.hash = fileInfo.hash;
            result.fileSize = fileInfo.fsize;
            result.mimeType = fileInfo.mimeType;
            result.putTime = fileInfo.putTime;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return result;
    }



    private QiniuGetAllResult getAllFilesFromCloud() {

        QiniuGetAllResult results = new QiniuGetAllResult();

        // 文件名前缀
        String prefix = "";
        // 每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        // 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        // 列举空间文件列表
        BucketManager.FileListIterator fileListIterator = generateBucketManager()
                .createFileListIterator(config.getBucketName(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            // 处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            if (items != null && items.length > 0) {
                results.results = new QiniuGetResult[items.length];
                for (int i = 0; i < items.length; i++) {
                    QiniuGetResult result = new QiniuGetResult();
                    FileInfo item = items[i];
                    result.key = item.key;
                    result.hash = item.hash;
                    result.fileSize = item.fsize;
                    result.mimeType = item.mimeType;
                    result.putTime = item.putTime;
                    result.endUser = item.endUser;

                    results.results[i] = result;
                }
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



    private String generateUploadToken(String objectKey) {
        return generateAuth(config.getAccessKey(), config.getSecureKey()).uploadToken(config.getBucketName(), objectKey,
                config.getExpireSeconds(), PUT_POLICY);
    }



    private BucketManager generateBucketManager() {
        return new BucketManager(generateAuth(config.getAccessKey(), config.getSecureKey()), generateConfiguration());
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
        public QiniuGetResult[] results;



        @Override
        public String toString() {
            return "QiniuGetAllResult [" + "results=" + results + ']';
        }
    }

}
