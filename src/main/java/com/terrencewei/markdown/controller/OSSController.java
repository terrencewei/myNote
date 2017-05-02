package com.terrencewei.markdown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;
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



    @PostMapping("/put/cloud")
    @ResponseBody
    public OSSOutput save(@RequestBody OSSInput pOSSObject) {
        return mOSSService.put(pOSSObject);
    }



    @PostMapping("/get/cloud")
    @ResponseBody
    public OSSOutput get(@RequestBody OSSInput pOSSObject) {
        return mOSSService.get(pOSSObject);
    }



    @PostMapping("/list/cloud")
    @ResponseBody
    public OSSOutput list(@RequestBody OSSInput pOSSObject) {
        return mOSSService.list();
    }

}