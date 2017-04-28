package com.terrencewei.markdown.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
    private QiniuUtils  mQiniuUtils;

    @Autowired
    private AliyunUtils mAliyunUtils;

    private enum OSSType {
        Qiniu, Aliyun

    }

    private OSSType currentOSS = OSSType.Qiniu;
    // private OSSType currentOSS = OSSType.Aliyun;



    @Override
    public OSSOutput put(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case Qiniu:
            response = mQiniuUtils.put(pOSSInput);
            break;
        case Aliyun:
            response = mAliyunUtils.put(pOSSInput);
            break;

        }
        return response;
    }



    @Override
    public OSSOutput get(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case Qiniu:
            response = mQiniuUtils.get(pOSSInput);
            break;
        case Aliyun:
            response = mAliyunUtils.get(pOSSInput);
            break;

        }
        return response;
    }



    @Override
    public OSSOutput list() {
        OSSOutput response = new OSSOutput();
        switch (currentOSS) {
        case Qiniu:
            response = mQiniuUtils.list(null);
            break;
        case Aliyun:
            response = mAliyunUtils.list(null);
            break;

        }
        return response;
    }
}
