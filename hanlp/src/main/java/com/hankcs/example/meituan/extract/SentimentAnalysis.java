package com.hankcs.example.meituan.extract;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.example.meituan.util.SentimentUtil;
import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;

import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author jianfei.yin
 * @create 2018-08-11 10:38 AM
 **/
public class SentimentAnalysis {
        public static final String MODEL_PATH = "C:\\space\\AI\\cluster\\HanLP\\data\\test\\SentimentModel.txt";
        private static final String sql =  "update meituan set positive=? where id = ?";

        public static void main(String[] args) throws IOException
        {
            int page = 0;
            String query = "select id,content from meituan limit "+(page*10000)+",10000";

            IClassifier classifier = new NaiveBayesClassifier(SentimentUtil.getModel(MODEL_PATH)); // 创建分类器，更高级的功能请参考IClassifier的接口定义

            try (Connection connection= JDBCUtil.getConn("cluster")){
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                boolean flag = true;
                while (flag){
                    List<Map<String, String>> extract = JDBCUtil.extract(statement.executeQuery(query));
                    if(extract.size()<10000){
                        flag =false;
                    }
                    for(Map<String,String> data:extract){
                        if(predict(classifier,data.get("content"))){
                           continue;
                        }
                        preparedStatement.setInt(1,0);
                        preparedStatement.setString(2,data.get("id"));
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    page++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        private static Boolean predict(IClassifier classifier, String text)
        {
            if("正面".equals(classifier.classify(text))){
                    return true;
            }
            return false;
        }

}
