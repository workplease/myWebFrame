package com.zzz.plugin.security;

import java.util.Set;

/**
 * Smart Security 接口
 */
public interface SmartSecurity {

    /**
     * 根据用户名获取密码
     * @param username
     * @return
     */
    String getPassword(String username);

    /**
     * 根据用户获取角色名集合
     * @param username
     * @return
     */
    Set<String> getRoleNameSet(String username);

    /**
     * 根据角色名获取权限名集合
     * @param roleName
     * @return
     */
    Set<String> getPermissionNameSet(String roleName);
}
