package com.terrencewei.markdown.util;

import com.terrencewei.markdown.bean.OSSInput;
import com.terrencewei.markdown.bean.OSSOutput;

/**
 * Created by terrencewei on 2017/04/28.
 */
public interface OSSUtils {

    /**
     * put object to Cloud
     * 
     * @param pInput
     * @return
     */
    public OSSOutput put(OSSInput pInput);



    /**
     * get object from cloud
     * 
     * @param pInput
     * @return
     */
    public OSSOutput get(OSSInput pInput);



    /**
     * list objects in cloud
     * 
     * @param pInput
     * @return
     */
    public OSSOutput list(OSSInput pInput);

}
