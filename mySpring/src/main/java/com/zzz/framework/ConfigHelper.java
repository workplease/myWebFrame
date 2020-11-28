package com.zzz.framework;


import com.zzz.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    /**
     * 获取 JDBC 驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取 JDBC URL
     * @return
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    /**
     * 获取 JDBC 用户名
     * @return
     */
    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    /**
     * 获取 JDBC 密码
     * @return
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     * @return
     */
    public static String getBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用 JDBC 路径
     * @return
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH);
    }

    /**
     * 获取应用静态资源路径
     * @return
     */
    public static String getAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH);
    }

    /**
     * 获取应用文件上传限制
     * @return
     */
    public static int getAppUploadLimit(){
        return PropsUtil.getInt(CONFIG_PROPS,ConfigConstant.APP_UPLOAD_LIMIT,10);
    }

    /**
     * 根据属性名获取 String 类型的属性值，带默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key,String defaultValue) {
        return PropsUtil.getString(CONFIG_PROPS, key, defaultValue);
    }

    /**
     * 根据属性名获取 boolean 类型的属性值
     */
    public static boolean getBoolean(String key) {
        return PropsUtil.getBoolean(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropsUtil.getString(CONFIG_PROPS, key);
    }
}
