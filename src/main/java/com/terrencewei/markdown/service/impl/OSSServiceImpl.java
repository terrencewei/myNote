package com.terrencewei.markdown.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;
import com.terrencewei.markdown.service.OSSService;
import com.terrencewei.markdown.util.AliyunUtils;
import com.terrencewei.markdown.util.QiniuUtils;

/**
 * Created by terrencewei on 4/21/17.
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Autowired
    private QiniuUtils          mQiniuUtils;

    @Autowired
    private AliyunUtils         mAliyunUtils;

    @Value("${ossType}")
    private String              currentOSS;
    private static final String QINIU_OSSTYPE  = "qiniu";
    private static final String ALIYUN_OSSTYPE = "aliyun";



    @Override
    public OSSOutput put(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case QINIU_OSSTYPE:
            response = mQiniuUtils.put(pOSSInput);
            break;
        case ALIYUN_OSSTYPE:
            response = mAliyunUtils.put(pOSSInput);
            break;

        }
        return response;
    }



    @Override
    public OSSOutput get(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case QINIU_OSSTYPE:
            response = mQiniuUtils.get(pOSSInput);
            break;
        case ALIYUN_OSSTYPE:
            response = mAliyunUtils.get(pOSSInput);
            break;

        }
        return response;
    }



    @Override
    public OSSOutput list() {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case QINIU_OSSTYPE:
            response = mQiniuUtils.list(null);
            break;
        case ALIYUN_OSSTYPE:
            response = mAliyunUtils.list(null);
            break;

        }
        return response;
    }
}
