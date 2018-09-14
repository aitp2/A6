package com.hankcs.example.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianfei.yin
 * @create 2018-08-01 9:11 AM
 **/
public class JDBCUtil {
    static {

    }
    public static Connection getConn(String database) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/"+database+"?useUnicode=true";
        String username = "root";
        String password = "";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static List<Map<String,String>> extract(ResultSet resultSet) throws SQLException {
        List<Map<String,String>> resultList=new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()){
            HashMap<String,String> res=new HashMap<>();
            for(int i=1;i<=columnCount;i++){
                res.put(metaData.getColumnName(i),resultSet.getString(i));
            }
            resultList.add(res);
        }
        return resultList;
    }
}
