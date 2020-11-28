package com.zzz.plugin.security;

import com.zzz.plugin.security.exception.AuthzException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security 助手类
 */
public final class SecurityHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityHelper.class);

    /**
     * 登录
     * @param username
     * @param password
     * @throws AuthzException
     */
    public static void login(String username,String password) throws AuthzException{
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null){
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            try{
                currentUser.login(token);
            }catch (AuthzException e){
                LOGGER.error("login failure",e);
                throw new AuthzException(e);
            }
        }
    }

    /**
     * 注销
     */
    public static void logout(){
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null){
            currentUser.logout();
        }
    }
}
