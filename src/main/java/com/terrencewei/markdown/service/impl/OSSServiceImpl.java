package com.terrencewei.markdown.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSObject;
import com.terrencewei.markdown.bean.OSSOutput;
import com.terrencewei.markdown.dao.OSSDao;
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
    public OSSOutput removeCloud(OSSInput pOSSInput) {
        // TODO:
        return new OSSOutput();
    }



    @Override
    public OSSOutput putLocal(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        if (pOSSInput != null && pOSSInput.getObjects() != null && pOSSInput.getObjects().length == 1) {
            OSSObject obj = new OSSObject();
            BeanUtils.copyProperties(pOSSInput.getObjects()[0], obj);
            try {
                obj.setSize(obj.getContent().getBytes("utf-8").length);
            } catch (UnsupportedEncodingException pE) {
                pE.printStackTrace();
            }
            obj.setUpdateTime(System.currentTimeMillis());
            if (mOSSDao.countByObjKey(obj.getObjKey()) > 0) {
                mOSSDao.deleteByObjKey(obj.getObjKey());
            }
            OSSObject result = mOSSDao.save(obj);
            response.setObjects(new OSSObject[] { new OSSObject(result.getObjKey(), result.getSize()) });
            response.setSuccess(true);
        }
        return response;
    }



    @Override
    public OSSOutput getLocal(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        if (pOSSInput != null && pOSSInput.getObjects() != null && pOSSInput.getObjects().length == 1) {
            String ObjKey = pOSSInput.getObjects()[0].getObjKey();
            if (mOSSDao.countByObjKey(ObjKey) > 0) {
                OSSObject[] objs = new OSSObject[] { new OSSObject() };
                BeanUtils.copyProperties(mOSSDao.findByObjKey(ObjKey).get(0), objs[0]);
                response.setObjects(objs);
                response.setSuccess(true);
            }
        }
        return response;
    }



    @Override
    public OSSOutput listLocal() {
        OSSOutput output = new OSSOutput();
        Iterator<OSSObject> itr = mOSSDao.findAll().iterator();
        if (itr != null) {
            List<OSSObject> objs = new ArrayList<OSSObject>();
            while (itr.hasNext()) {
                OSSObject obj = new OSSObject();
                BeanUtils.copyProperties(itr.next(), obj);
                objs.add(obj);
            }
            if (objs.size() > 0) {
                Collections.reverse(objs);
                output.setSuccess(true);
                output.setObjects(objs.toArray(new OSSObject[objs.size()]));
            }
        }
        return output;
    }



    @Override
    public OSSOutput removeLocal(OSSInput pOSSInput) {
        OSSOutput response = new OSSOutput();
        if (pOSSInput != null && pOSSInput.getObjects() != null && pOSSInput.getObjects().length == 1) {
            List<OSSObject> deletedEntities = null;
            OSSObject obj = new OSSObject();
            BeanUtils.copyProperties(pOSSInput.getObjects()[0], obj);
            if (mOSSDao.countByObjKey(obj.getObjKey()) > 0) {
                deletedEntities = mOSSDao.deleteByObjKey(obj.getObjKey());
                if (!deletedEntities.isEmpty()) {
                    response.setObjects(new OSSObject[] {
                            new OSSObject(deletedEntities.get(0).getObjKey(), deletedEntities.get(0).getSize()) });
                    response.setSuccess(true);
                }
            }
        }
        return response;
    }
}
