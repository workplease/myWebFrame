package com.zzz.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载属性文件
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件：props 和 is 初始化为 null，通过文件名创建输入流并且赋给 is，如果不存在抛出异常；存在文件就将其注入到 props 中
     * @param fileName
     * @return
     */
    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null){
                throw new FileNotFoundException(fileName + " file is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties file failure",e);
        }finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性（默认为空字符串）
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key){
        return getString(props,key,"");
    }

    /**
     * 获取字符类型（可指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties props,String key,String defaultValue){
        String value = defaultValue;
        if (props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性（默认值为0）
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props,String key){
        return getInt(props, key,0);
    }

    /**
     * 获取数值型属性（可指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties props,String key,int defaultValue){
        int value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取布尔型属性（默认值是 false）
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props, key,false);
    }

    /**
     * 获取布尔型属性（可指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties props,String key,boolean defaultValue){
        boolean value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}
