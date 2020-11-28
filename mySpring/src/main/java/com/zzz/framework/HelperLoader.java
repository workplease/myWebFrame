package com.zzz.framework;

import com.zzz.framework.helper.*;
import com.zzz.framework.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 */
public final class HelperLoader {

    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
