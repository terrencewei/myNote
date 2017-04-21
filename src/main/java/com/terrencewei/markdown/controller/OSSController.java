package com.terrencewei.markdown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.terrencewei.markdown.bean.OSSObject;
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
    public boolean handleBlogList(@RequestBody OSSObject pOSSObject) {
        if (pOSSObject != null && StringUtils.isNotBlank(pOSSObject.getKey()) && pOSSObject.getData() != null) {
            return mOSSService.save2OSS(pOSSObject);
        }
        return false;
    }

}