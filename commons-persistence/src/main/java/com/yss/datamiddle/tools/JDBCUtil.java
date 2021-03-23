package com.yss.datamiddle.tools;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: JDBC工具类  （通常将方法设置为静态方法，不然还要通过new 一个对象来调用方法，不优雅,类加载时即加载）
 *  1.获取连接
 *  2.关闭释放jdbc使用的资源
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public final class JDBCUtil {

    /**
     * 1.获取jdbc连接的方法getconnection （通过JDBCUtil.getConnection（）来获取一个JDBC的连接）
     * driverName, url, username, password
     * mysql：
     * String driverName = "com.mysql.cj.jdbc.Driver";
     * String url = "jdbc:mysql://192.168.79.139:3306/yss_data_middle?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";
     * String username = "root";
     * String password = "root";
     * <p>
     * hive：
     * driverName = "org.apache.hive.jdbc.HiveDriver";
     * String url = "jdbc:hive2://localhost:10000/default";
     * String username = "hive";
     * String password = ""
     */
    public static Connection getConnection(String driverName, String url, String username, String password) {
        try {
            // 1、加载驱动
            Class.forName(driverName);
            // String url = "jdbc:mysql://ip:port/数据库名称";(port通常是3306，ip需输入，数据库名称需输入，用户名密码)
            // 2、获取连接，url为jdbc连接：连接的mysql，服务器ip+数据库的名称   再由驱动管理者获取连接（url，username，password）
            // url = "jdbc:mysql://" + ip + ":" + port + "/" + mysqldatabase;
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) { // 捕捉所有的异常
            log.error("数据库连接异常：" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 2、关闭（栈式关闭），释放资源的方法close （若不存在使用下列资源，传递参数为null即可，通过JDBCUtil.close()关闭资源）
     * rs 为结果集，通过JDBC查到的结果集，使用后需关闭释放资源
     * stmt 为开启的sql语句
     * connection 为jdbc的连接
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("数据源关闭【ResultSet】异常：" + e.getMessage(), e);
            }
        }
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("数据源关闭【Statement】异常：" + e.getMessage(), e);
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("数据源关闭【Connection】异常：" + e.getMessage(), e);
            }
        }
    }

    /**
     * 3、执行
     */
    public static List<Map<String, Object>> execute(String driverName, String url, String username, String password, String sql) {

        /*String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.79.139:3306/yss_data_middle?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=true";
        String username = "root";
        String password = "root";
        String sql = "select * from t_biz_norm";*/
        Connection connection = getConnection(driverName, url, username, password);
        return null != connection ? executeWithConnection(connection, sql) : null;
    }

    public static List<Map<String, Object>> executeWithConnection(Connection connection, String sql) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> columnList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                Map<String, Object> map = new HashMap<>(4);
                String columnName = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                map.put("columnName", columnName);
                map.put("columnType", columnType);
                columnList.add(map);
            }
            while (resultSet.next()) {
                for (Map<String, Object> columnMap : columnList) {
                    String columnName = (String) columnMap.get("columnName");
                    Object object = resultSet.getObject(columnName);
                    columnMap.put("columnValue", object);
                }
            }
        } catch (SQLException e) {
            log.error("数据库操作异常：" + e.getMessage(), e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return columnList;
    }
}
