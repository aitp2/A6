package com.hankcs.example;

import com.hankcs.example.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianfei.yin
 * @create 2018-08-06 4:56 PM
 **/
public class DeleteTable {
    public static final String QUERY = "select id ,content from news order by id";
    public static void main(String[] args) {
        try (Connection connection= JDBCUtil.getConn("cluster")){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY);
            List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
            HashMap<String, List<String>> ids=new HashMap<>();
            for(Map<String,String> m:resultList){
                String content = m.get("content");
                List<String> id = ids.containsKey(content)? ids.get(content):new ArrayList<String>();
                id.add(m.get("id"));
                ids.put(content,id);
            }
            for(List<String> id:ids.values()){
                id.remove(0);
                if(!id.isEmpty()){
                    for(String i: id){
                        statement.execute("delete from news where id ='"+i+"'");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
