package com.terrencewei.markdown.service.impl;

import com.terrencewei.markdown.bean.OSSObject;
import com.terrencewei.markdown.dao.OSSDao;
import com.terrencewei.markdown.model.OSSEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;
import com.terrencewei.markdown.service.OSSService;
import com.terrencewei.markdown.util.AliyunUtils;
import com.terrencewei.markdown.util.QiniuUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by terrencewei on 4/21/17.
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Autowired
    private QiniuUtils          mQiniuUtils;

    @Autowired
    private AliyunUtils         mAliyunUtils;

    @Autowired
    private OSSDao              mOSSDao;

    @Value("${ossType}")
    private String              currentOSS;
    private static final String QINIU_OSSTYPE  = "qiniu";
    private static final String ALIYUN_OSSTYPE = "aliyun";



    @Override
    public OSSOutput putCloud(OSSInput pOSSInput) {
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
    public OSSOutput getCloud(OSSInput pOSSInput) {
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
    public OSSOutput listCloud() {
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



    @Override
    public OSSOutput putLocal(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        if (pOSSInput != null && pOSSInput.getObjects() != null && pOSSInput.getObjects().length == 1) {
            OSSEntity entity = new OSSEntity();
            BeanUtils.copyProperties(pOSSInput.getObjects()[0], entity);
            entity.setUpdateTime(System.currentTimeMillis());
            if (mOSSDao.countByKey(entity.getKey()) > 0) {
                mOSSDao.deleteByKey(entity.getKey());
            }
            mOSSDao.save(entity);
            response.setSuccess(true);
        }
        return response;
    }



    @Override
    public OSSOutput getLocal(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        if (pOSSInput != null && pOSSInput.getObjects() != null && pOSSInput.getObjects().length == 1) {
            String key = pOSSInput.getObjects()[0].getKey();
            if (mOSSDao.countByKey(key) > 0) {
                OSSObject[] objs = new OSSObject[1];
                objs[0] = new OSSObject();
                BeanUtils.copyProperties(mOSSDao.findByKey(key).get(0), objs[0]);
                response.setObjects(objs);
                response.setSuccess(true);
            }
        }
        return response;
    }



    @Override
    public OSSOutput listLocal() {
        OSSOutput output = new OSSOutput();
        Iterator<OSSEntity> itr = mOSSDao.findAll().iterator();
        if (itr != null) {
            List<OSSObject> objs = new ArrayList<OSSObject>();
            while (itr.hasNext()) {
                OSSObject obj = new OSSObject();
                BeanUtils.copyProperties(itr.next(), obj);
                objs.add(obj);
            }
            if (objs.size() > 0) {
                output.setSuccess(true);
                output.setObjects(objs.toArray(new OSSObject[objs.size()]));
            }
        }
        return output;
    }
}
