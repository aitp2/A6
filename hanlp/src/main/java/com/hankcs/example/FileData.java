package com.hankcs.example;

import com.hankcs.example.util.JDBCUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-03 2:55 PM
 **/
public class FileData
{
    public static void main(String[] args) {
        try(BufferedReader reader=new BufferedReader(new FileReader(new File("C:\\space\\AI\\toutiao_cat_data.txt")))){
            String temp;
            int i=1;
            List<String> strings=new ArrayList<>();
            while ((temp=reader.readLine())!=null){
                strings.add(temp);
                if(i%2000==0){
                    insert(strings);
//                    Thread.sleep(2000);
                    strings.clear();
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void insert(List<String> list){
        String sql =  "insert into news (content,category,tag) values (?,?,?)";

        try (Connection conn = JDBCUtil.getConn("cluster")){
            conn.setAutoCommit(true);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            for(String s:list){
                String[] split = s.split("_!_");
                if(!"news_game".equals(split[2])){
                    continue;
                }
                preparedStatement.setString(1,split[3]);
                preparedStatement.setString(2,split[2]);
                if(split.length>=5){
                    preparedStatement.setString(3,split[4]);
                }else {
                    preparedStatement.setString(3,null);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
