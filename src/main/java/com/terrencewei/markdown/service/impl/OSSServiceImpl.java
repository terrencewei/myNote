package com.terrencewei.markdown.service.impl;

import com.terrencewei.markdown.bean.OSSObject;
import com.terrencewei.markdown.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terrencewei.markdown.service.OSSService;

import java.io.File;

/**
 * Created by terrencewei on 4/21/17.
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Override
    public boolean save2OSS(OSSObject pOSSObject) {
        QiniuUtils.QiniuPutResult result = QiniuUtils.uploadFile2Cloud(pOSSObject.getData().toString().getBytes(),
                pOSSObject.getKey());
        return result != null;
    }
}
