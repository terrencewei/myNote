package com.terrencewei.markdown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.terrencewei.markdown.bean.OSSObject;
import com.terrencewei.markdown.service.impl.OSSServiceImpl;

/**
 * Created by terrencewei on 2017/04/21.
 */
@Controller
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    private OSSServiceImpl mOSSServiceImpl;



    @PostMapping("/save")
    @ResponseBody
    public boolean handleBlogList(@RequestBody OSSObject pOSSObject) {
        // TODO:
        System.out.println(pOSSObject);
        return false;
    }

}