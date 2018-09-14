package com.hankcs.example.aitp;

import com.hankcs.example.util.JDBCUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author jianfei.yin
 * @create 2018-08-27 8:24 PM
 **/
public class SQLInsert {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Users\\jianfei.yin\\Desktop\\ads.txt")))) {
            StringBuilder builder=new StringBuilder();
            String tmp;
            while ((tmp=reader.readLine())!=null){
                builder.append(tmp);
            }

            try (Connection connection= JDBCUtil.getConn("aitp2")){
                Statement statement = connection.createStatement();
                statement.execute(builder.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
