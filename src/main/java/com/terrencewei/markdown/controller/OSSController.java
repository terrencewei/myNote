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
    public OSSOutput putCloud(@RequestBody OSSInput pOSSObject) {
        return mOSSService.putCloud(pOSSObject);
    }



    @PostMapping("/get/cloud")
    @ResponseBody
    public OSSOutput getCloud(@RequestBody OSSInput pOSSObject) {
        return mOSSService.getCloud(pOSSObject);
    }



    @PostMapping("/list/cloud")
    @ResponseBody
    public OSSOutput listCloud(@RequestBody OSSInput pOSSObject) {
        return mOSSService.listCloud();
    }



    @PostMapping("/remove/cloud")
    @ResponseBody
    public OSSOutput removeCloud(@RequestBody OSSInput pOSSObject) {
        return mOSSService.removeCloud(pOSSObject);
    }



    @PostMapping("/put/local")
    @ResponseBody
    public OSSOutput putLocal(@RequestBody OSSInput pOSSObject) {
        return mOSSService.putLocal(pOSSObject);
    }



    @PostMapping("/get/local")
    @ResponseBody
    public OSSOutput getLocal(@RequestBody OSSInput pOSSObject) {
        return mOSSService.getLocal(pOSSObject);
    }



    @PostMapping("/list/local")
    @ResponseBody
    public OSSOutput listLocal(@RequestBody OSSInput pOSSObject) {
        return mOSSService.listLocal();
    }



    @PostMapping("/remove/local")
    @ResponseBody
    public OSSOutput removeLocal(@RequestBody OSSInput pOSSObject) {
        return mOSSService.removeLocal(pOSSObject);
    }

}