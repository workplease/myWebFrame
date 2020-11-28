package com.zzz.plugin.soap;

import com.zzz.framework.ConfigHelper;

/**
 * 从配置文件中获取相关属性
 */
public class SoapConfig {

    public static boolean isLog(){
        return ConfigHelper.getBoolean(SoapConstant.LOG);
    }
}
