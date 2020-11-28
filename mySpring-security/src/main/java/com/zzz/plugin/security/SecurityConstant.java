package com.zzz.plugin.security;

/**
 * 常量接口
 */
public interface SecurityConstant {

    String REALMS = "zzz.plugin.security.realms";
    String REALMS_JDBC = "jdbc";
    String REALMS_CUSTOM = "custom";

    String SMART_SECURITY = "zzz.plugin.security.custom.class";

    String JDBC_AUTHC_QUERY = "zzz.plugin.security.jdbc.authc_query";
    String JDBC_ROLES_QUERY = "zzz.plugin.security.jdbc.roles_query";
    String JDBC_PERMISSIONS_QUERY = "zzz.plugin.security.jdbc.permissions_query";

    String CACHE = "zzz.plugin.security.cache";
}
