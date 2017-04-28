package com.terrencewei.markdown.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by terrencewei on 2017/04/28.
 */
@Component
@ConfigurationProperties(prefix = "ossConfig.aliyun")
public class OSSConfigAliyun extends OSSConfig {
}
