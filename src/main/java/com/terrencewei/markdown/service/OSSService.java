package com.terrencewei.markdown.service;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;

/**
 * Created by terrencewei on 4/21/17.
 */
public interface OSSService {

    public OSSOutput putCloud(OSSInput pOSSInput);



    public OSSOutput getCloud(OSSInput pOSSInput);



    public OSSOutput listCloud();



    public OSSOutput removeCloud(OSSInput pOSSInput);



    public OSSOutput putLocal(OSSInput pOSSInput);



    public OSSOutput getLocal(OSSInput pOSSInput);



    public OSSOutput listLocal();



    public OSSOutput removeLocal(OSSInput pOSSInput);

}
