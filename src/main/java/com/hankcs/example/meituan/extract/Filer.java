package com.hankcs.example.meituan.extract;

import com.hankcs.example.util.JDBCUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jianfei.yin
 * @create 2018-08-11 3:24 AM
 **/
public class Filer {

    private static final String NEG="C:\\space\\AI\\cluster\\HanLP\\data\\test\\resturants\\负面";
    private static final String POS="C:\\space\\AI\\cluster\\HanLP\\data\\test\\resturants\\正面";
    private volatile static AtomicInteger neg = new AtomicInteger(0);
    private volatile static AtomicInteger pos = new AtomicInteger(0);
    public static void main(String[] args) {
        new Thread(new Positive()).start();
        new Thread(new Nagitive()).start();

    }

    public static class Nagitive implements Runnable{

        private Connection connection;
        public Nagitive(){
            connection = JDBCUtil.getConn("cluster");
        }
        @Override
        public void run() {
            int page = 0;
            String upper = "select star,content from meituan where star <=25 limit "+(page*10000)+",10000";

            try {
                Statement statement = connection.createStatement();
                boolean start = true;
                while (start){
                    ResultSet resultSet = statement.executeQuery(upper);
                    List<Map<String, String>> mapList = JDBCUtil.extract(resultSet);
                    if(mapList.size()<10000){
                        start =false;
                    }
                    for(Map<String,String> positive: mapList){
                        File f = new File(NEG+"\\"+"neg."+ neg.getAndIncrement()+".txt");
                        try (BufferedWriter br = new BufferedWriter(new FileWriter(f))){
                            br.write(positive.get("content"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                page++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Positive implements Runnable{

        private Connection connection;
        public Positive(){
            connection = JDBCUtil.getConn("cluster");
        }
        @Override
        public void run() {
            int page = 0;
            String upper = "select star,content from meituan where star >40 limit "+(page*10000)+",10000";

            try {
                Statement statement = connection.createStatement();
                boolean stop = false;
                while (!stop){
                    ResultSet resultSet = statement.executeQuery(upper);
                    List<Map<String, String>> extract = JDBCUtil.extract(resultSet);
                    if(extract.size()<10000){
                        stop = true;
                    }
                    for(Map<String,String> co: extract){
                        File f = new File(POS+"\\"+"pos."+pos.getAndIncrement()+".txt");
                        try (BufferedWriter br = new BufferedWriter(new FileWriter(f))){
                            br.write(co.get("content"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                page++;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




}
