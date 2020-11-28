package com.zzz.plugin.security;

import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * Smart Security 插件
 */
public class SmartSecurityPlugin implements ServletContainerInitializer {

    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        //设置初始化参数
        servletContext.setInitParameter("shiroConfigLocations","class-path:smart-security.ini");
        //注册 Listener
        servletContext.addListener(EnvironmentLoaderListener.class);
        //注册 Filter
        FilterRegistration.Dynamic smartSecurityFilter = servletContext.addFilter("SmartSecurityFilter",SmartSecurityFilter.class);
        smartSecurityFilter.addMappingForUrlPatterns(null,false,"/*");
    }
}
