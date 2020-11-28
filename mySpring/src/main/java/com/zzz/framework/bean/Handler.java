package com.zzz.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装 Action 类
 */
public class Handler {

    //Controller 类型
    private Class<?> controllerClass;
    //Action 方法
    private Method actionMethod;

    public Handler(Class<?> controllerClass,Method actionMethod){
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
