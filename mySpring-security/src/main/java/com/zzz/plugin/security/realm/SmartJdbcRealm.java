package com.zzz.plugin.security.realm;

import com.zzz.framework.helper.DatabaseHelper;
import com.zzz.plugin.security.SecurityConfig;
import com.zzz.plugin.security.password.Md5CredentialMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * 基于 Smart 的 JDBC Realm （需要提供相关 zzz.plugin.security.jdbc.* 配置项）
 */
public class SmartJdbcRealm extends JdbcRealm {

    public SmartJdbcRealm(){
        super.setDataSource(DatabaseHelper.getDataSource());
        super.setAuthenticationQuery(SecurityConfig.getJdbcAuthcQuery());
        super.setUserRolesQuery(SecurityConfig.getJdbcRolesQuery());
        super.setPermissionsQuery(SecurityConfig.getJdbcPermissionsQuery());
        super.setPermissionsLookupEnabled(true);
        //使用 MD5 加密算法
        super.setCredentialsMatcher(new Md5CredentialMatcher());
    }
}
