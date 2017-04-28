package com.terrencewei.markdown.service;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;

/**
 * Created by terrencewei on 4/21/17.
 */
public interface OSSService {

    public OSSOutput put(OSSInput pOSSInput);



    public OSSOutput get(OSSInput pOSSInput);



    public OSSOutput list();

}
