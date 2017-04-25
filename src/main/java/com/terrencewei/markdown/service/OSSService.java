package com.terrencewei.markdown.service;

import com.terrencewei.markdown.bean.OSSRequest;
import com.terrencewei.markdown.bean.OSSResponse;

/**
 * Created by terrencewei on 4/21/17.
 */
public interface OSSService {

    public OSSResponse save(OSSRequest pOSSRequest);



    public OSSResponse get(OSSRequest pOSSRequest);



    public OSSResponse getAll();

}
