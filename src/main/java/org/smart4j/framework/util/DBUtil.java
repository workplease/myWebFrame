package org.smart4j.framework.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库的常用操作
 */
public final class DBUtil {

    //数据库配置
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/demo";
    private static final String username = "root";
    private static final String password = "root";

    //定义一个用于放置数据库连接的局部变量（使每个线程都拥有自己的连接）
    private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();

    //获取连接
    public static Connection getConnection(){
        Connection conn = connContainer.get();
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connContainer.set(conn);
        }
        return conn;
    }

    //关闭连接
    public static void closeConnection(){
        Connection conn = connContainer.get();
        try{
            if (conn != null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connContainer.set(conn);
        }
    }
}
