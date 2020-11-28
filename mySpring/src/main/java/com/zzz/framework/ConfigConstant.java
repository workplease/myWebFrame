package com.zzz.framework;

/**
 * 提供相关配置项常量
 */
public interface ConfigConstant {

    String CONFIG_FILE = "framework.properties";

    String JDBC_DRIVER = "zzz.framework.jdbc.driver";
    String JDBC_URL = "zzz.framework.jdbc.url";
    String JDBC_USERNAME = "zzz.framework.jdbc.username";
    String JDBC_PASSWORD = "zzz.framework.jdbc.password";

    String APP_BASE_PACKAGE = "zzz.framework.app.base_package";
    String APP_JSP_PATH = "zzz.framework.app.jsp_path";
    String APP_ASSET_PATH = "zzz.framework.app.asset_path";

    String APP_UPLOAD_LIMIT = "zzz,framework.app.upload_limit";
}
