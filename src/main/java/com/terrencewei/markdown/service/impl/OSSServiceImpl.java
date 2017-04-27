package com.terrencewei.markdown.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terrencewei.markdown.bean.OSSRequest;
import com.terrencewei.markdown.bean.OSSResponse;
import com.terrencewei.markdown.service.OSSService;
import com.terrencewei.markdown.util.QiniuUtils;
import com.terrencewei.markdown.util.StringUtils;

/**
 * Created by terrencewei on 4/21/17.
 */
@Service
public class OSSServiceImpl implements OSSService {

    @Autowired
    private QiniuUtils mQiniuUtils;



    @Override
    public OSSResponse save(OSSRequest pOSSRequest) {
        OSSResponse response = new OSSResponse();
        QiniuUtils.QiniuPutResult result = mQiniuUtils.uploadFile2Cloud(pOSSRequest.getObjData().toString().getBytes(),
                pOSSRequest.getObjKey());
        if (result != null) {
            response.setSuccess(true);
            response.setData(result);
        }
        return response;
    }



    @Override
    public OSSResponse get(OSSRequest pOSSRequest) {
        OSSResponse response = new OSSResponse();
        String downloadUrl = mQiniuUtils.downloadFileFromCloud(pOSSRequest.getObjKey());
        if (StringUtils.isNotBlank(downloadUrl)) {
            try {
                response.setData(new String(downloadBytesByHttp(downloadUrl), "UTF-8"));
                response.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }



    public byte[] downloadBytesByHttp(String pUrl) {
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



    @Override
    public OSSResponse getAll() {
        OSSResponse response = new OSSResponse();
        QiniuUtils.QiniuGetAllResult result = mQiniuUtils.getAllFilesFromCloud();
        if (result != null) {
            response.setSuccess(true);
            response.setData(result);
        }
        return response;
    }
}
