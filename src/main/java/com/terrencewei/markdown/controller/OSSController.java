package com.terrencewei.markdown.controller;

import com.terrencewei.markdown.bean.OSSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.terrencewei.markdown.bean.OSSRequest;
import com.terrencewei.markdown.service.OSSService;
import com.terrencewei.markdown.util.StringUtils;

/**
 * Created by terrencewei on 2017/04/21.
 */
@Controller
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    private OSSService mOSSService;



    @PostMapping("/save")
    @ResponseBody
    public OSSResponse save(@RequestBody OSSRequest pOSSObject) {
        OSSResponse response = new OSSResponse();
        if (pOSSObject != null && StringUtils.isNotBlank(pOSSObject.getObjKey()) && pOSSObject.getObjData() != null) {
            response = mOSSService.save(pOSSObject);
        }
        return response;
    }



    @PostMapping("/get")
    @ResponseBody
    public OSSResponse get(@RequestBody OSSRequest pOSSObject) {
        if (pOSSObject != null && StringUtils.isNotBlank(pOSSObject.getObjKey())) {
            return mOSSService.get(pOSSObject);
        } else {
            return mOSSService.getAll();
        }
    }

}