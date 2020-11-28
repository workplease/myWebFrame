package com.zzz.plugin.security;

import com.zzz.plugin.security.realm.SmartCustomRealm;
import com.zzz.plugin.security.realm.SmartJdbcRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 安全过滤器
 */
public class SmartSecurityFilter extends ShiroFilter {

    @Override
    public void init() throws Exception {
        super.init();
        WebSecurityManager webSecurityManager = super.getSecurityManager();

    }

    private void setRealms(WebSecurityManager webSecurityManager){
        //读取 zzz.plugin.security.realms 配置项
        String securityRealms = SecurityConfig.getRealms();
        if (securityRealms != null){
            //根据逗号进行拆分
            String[] securityRealmArray = securityRealms.split(",");
            if (securityRealmArray.length > 0){
                //使 Realm 具备唯一性与顺序性
                Set<Realm> realms = new LinkedHashSet<Realm>();
                for (String securityRealm : securityRealmArray){
                    if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_JDBC)){
                        //添加基于 JDBC 的 Realm，需配置相关 SQL 查询语句
                        addJdbcRealm(realms);
                    }else if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_CUSTOM)){
                        //添加基于定制化的 Realm，需实现 SmartSecurity 接口
                        addCustomRealm(realms);
                    }
                }
                RealmSecurityManager realmSecurityManager = (RealmSecurityManager) webSecurityManager;
                //设置 Realm
                realmSecurityManager.setRealms(realms);
            }
        }
    }

    private void addJdbcRealm(Set<Realm> realms){
        //添加自己实现的基于 JDBC 的 Ream
        SmartJdbcRealm smartJdbcRealm = new SmartJdbcRealm();
        realms.add(smartJdbcRealm);
    }

    private void addCustomRealm(Set<Realm> realms){
        //读取 zzz.plugin.security.custom.class 配置项
        SmartSecurity smartSecurity = SecurityConfig.getSmartSecurity();
        //添加自己的 Realm
        SmartCustomRealm smartCustomRealm = new SmartCustomRealm();
        realms.add(smartCustomRealm);
    }

    private void setCache(WebSecurityManager webSecurityManager){
        //读取 zzz.plugin.security.cache 配置项
        if (SecurityConfig.isCache()){
            CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) webSecurityManager;
            //使用基于内存的 CacheManager
            CacheManager cacheManager = new MemoryConstrainedCacheManager();
            cachingSecurityManager.setCacheManager(cacheManager);
        }
    }
}
