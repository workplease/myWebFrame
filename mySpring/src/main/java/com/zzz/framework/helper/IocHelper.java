package com.zzz.framework.helper;

import com.zzz.framework.annotation.Inject;
import com.zzz.framework.util.ArrayUtil;
import com.zzz.framework.util.CollectionUtil;
import com.zzz.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public final class IocHelper {

    static {
        //获取所有的 Bean 类与 Bean 实例之间的映射关系（简称 Bean Map）
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isEmpty(beanMap)){
            //遍历 Bean Map
            for (Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
                //从 BeanMap 中获取 bean 类与 bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取 Bean 类定义的所有成员变量（简称 Bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)){
                    //遍历 Bean Field
                    for (Field beanField : beanFields){
                        //判断当前 Bean Field 中是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)){
                            //在 Bean Map 中获取 Bean Field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                //通过反射初始化 BeanField 的值
                                ReflectionUtil.setField(beanFieldInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
