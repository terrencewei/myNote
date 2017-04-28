package com.terrencewei.markdown.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.terrencewei.markdown.bean.OSSConfigAliyun;
import com.terrencewei.markdown.bean.OSSObject;

/**
 * Created by terrencewei on 2017/04/27.
 *
 * 阿里云 OSS
 *
 * https://help.aliyun.com/document_detail/32008.html
 */
@Component
public class AliyunUtils extends OSSUtilsImpl {

    @Autowired
    protected OSSConfigAliyun config;



    public AliyunUtils() {
        super.config = this.config;
    }



    @Override
    protected OSSObject putSingle(String pKey, String pContent) {
        OSSObject output = null;

        OSSClient ossClient = getClient();

        PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName(), pKey,
                new ByteArrayInputStream(pContent.getBytes()));

        // 上传回调参数
        Callback callback = new Callback();
        callback.setCallbackUrl("/put/success");
        callback.setCallbackHost("localhost");
        callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
        callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
        callback.addCallbackVar("x:var1", "value1");
        callback.addCallbackVar("x:var2", "value2");
        putObjectRequest.setCallback(callback);

        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);

        // 读取上传回调返回的消息内容
        byte[] buffer = new byte[1024];
        try {
            putObjectResult.getCallbackResponseBody().read(buffer);
            // 一定要close，否则会造成连接资源泄漏
            putObjectResult.getCallbackResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Aliyun put result call back is: " + new String(buffer));

        // 关闭client
        ossClient.shutdown();

        output = new OSSObject();
        output.setKey(pKey);
        output.setSize(new Long(pContent.getBytes().length).intValue());
        return output;
    }



    @Override
    protected OSSObject getSingle(String pKey) {
        OSSObject output = null;

        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(config.getEndPoint(), config.getAccessKey(), config.getSecureKey());
        com.aliyun.oss.model.OSSObject getResult = ossClient.getObject(config.getBucketName(), pKey);
        // 读Object内容
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResult.getObjectContent()));
            while (true) {
                String line = null;
                line = reader.readLine();

                if (line == null) {
                    break;
                }
                content.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭client
        ossClient.shutdown();

        if (content.length() > 0) {
            output = new OSSObject();
            output.setContent(content.toString());
        }
        return output;
    }



    @Override
    protected OSSObject[] listAll() {
        OSSObject[] outputs = null;
        // 创建OSSClient实例
        OSSClient ossClient = getClient();
        // 列举Object
        List<OSSObjectSummary> sums = ossClient.listObjects(config.getBucketName()).getObjectSummaries();
        if (sums != null && !sums.isEmpty()) {
            outputs = new OSSObject[sums.size()];
            for (int i = 0; i < sums.size(); i++) {
                OSSObject obj = new OSSObject();
                OSSObjectSummary sum = sums.get(i);

                obj.setKey(sum.getKey());

                outputs[i] = obj;
            }
        }
        // 关闭client
        ossClient.shutdown();
        return outputs;
    }



    private OSSClient getClient() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(1);
        // 10秒
        conf.setSocketTimeout(config.getExpireSeconds() * 1000);
        return new OSSClient(config.getEndPoint(), config.getAccessKey(), config.getSecureKey(), conf);
    }
}
