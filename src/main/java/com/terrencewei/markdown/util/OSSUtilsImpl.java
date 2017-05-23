package com.terrencewei.markdown.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;

import com.terrencewei.markdown.bean.*;

/**
 * Created by terrencewei on 2017/04/28.
 */
public abstract class OSSUtilsImpl implements OSSUtils {

    @Autowired
    protected OSSConfigDefault defaultConfig;

    protected OSSConfig        config;



    public OSSUtilsImpl() {
        this.config = defaultConfig;
    }



    protected abstract OSSObject putSingle(String pKey, String pContent);



    protected abstract OSSObject getSingle(String pKey);



    protected abstract OSSObject[] listAll();



    @Override
    public OSSOutput put(OSSInput pInput) {
        OSSOutput output = null;

        if (pInput != null && pInput.getObjects() != null) {
            if (StringUtils.isNotBlank(pInput.getBucketName())) {
                config.setBucketName(pInput.getBucketName());
            }
            if (pInput.getObjects().length == 1 && StringUtils.isNotBlank(pInput.getObjects()[0].getObjKey())) {
                // put single
                OSSObject inputObj = pInput.getObjects()[0];
                OSSObject outputObj = putSingle(inputObj.getObjKey(), inputObj.getContent());
                if (outputObj != null) {
                    OSSObject[] objs = new OSSObject[1];
                    objs[0] = outputObj;

                    output = new OSSOutput();
                    output.setObjects(objs);
                    output.setSuccess(true);
                }
            } else if (pInput.getObjects().length > 1) {
                // put multi
                // TODO:
                output.setMsg("do not support put multi yet.");
            }

        }
        return output;
    }



    @Override
    public OSSOutput get(OSSInput pInput) {
        OSSOutput output = null;
        if (pInput != null && pInput.getObjects() != null) {
            if (StringUtils.isNotBlank(pInput.getBucketName())) {
                config.setBucketName(pInput.getBucketName());
            }
            if (pInput.getObjects().length == 1 && StringUtils.isNotBlank(pInput.getObjects()[0].getObjKey())) {
                // get single
                OSSObject inputObj = pInput.getObjects()[0];
                OSSObject outputObj = getSingle(inputObj.getObjKey());

                if (outputObj != null) {
                    OSSObject[] objs = new OSSObject[1];
                    objs[0] = outputObj;

                    output = new OSSOutput();
                    output.setObjects(objs);
                    output.setSuccess(true);
                }
            } else if (pInput.getObjects().length > 1) {
                // get multi
                // TODO:
                output.setMsg("do not support get multi yet.");
            }
        }
        return output;
    }



    @Override
    public OSSOutput list(OSSInput pInput) {
        OSSOutput output = null;
        if (pInput != null && StringUtils.isNotBlank(pInput.getBucketName())) {
            config.setBucketName(pInput.getBucketName());
        }

        OSSObject[] outputObjs = listAll();

        if (outputObjs != null && outputObjs.length > 0) {
            output = new OSSOutput();
            output.setObjects(outputObjs);
            output.setSuccess(true);
        }
        return output;
    }



    public String getContentByURL(String pUrl) {
        byte[] bytes = getContentBytesByURL(pUrl);
        return bytes != null && bytes.length > 0 ? new String(bytes) : null;
    }



    public byte[] getContentBytesByURL(String pUrl) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        URLConnection conn = null;
        try {
            conn = new URL(pUrl).openConnection();
            in = new BufferedInputStream(conn.getInputStream());
            out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            byte[] content = out.toByteArray();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
