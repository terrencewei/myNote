package com.terrencewei.markdown.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by terrencewei on 2017/04/21.
 */
@Component
@ConfigurationProperties(prefix = "ossConfig.default")
public class OSSConfigDefault extends OSSConfig {

}
