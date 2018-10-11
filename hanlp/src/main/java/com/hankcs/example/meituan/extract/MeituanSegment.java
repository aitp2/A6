package com.hankcs.example.meituan.extract;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.example.util.SegmentUtil;
import com.hankcs.example.util.StopWord;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.mysql.cj.util.StringUtils;

import java.sql.*;
import java.util.*;

/**
 * @author jianfei.yin
 * @create 2018-08-04 9:40 AM
 **/
public class MeituanSegment {

    private static final String UPDATE_SQL="update meituan set segments=? where id=? ";
    private static final String SELECT_SQL="select id, content from meituan order by id";
    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("cluster")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_SQL);
            List<Map<String, String>>  extract= JDBCUtil.extract(resultSet);
            extract(extract);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void customerWord(){
        String[] customeWords={};
        Arrays.stream(customeWords).forEach(w->CustomDictionary.add(w,"nr 10"));
    }

    public static void extract(List<Map<String,String>> tasks){
        Segment segment = SegmentUtil.nshort();
        customerWord();
        Connection conn=JDBCUtil.getConn("cluster");
        HashMap<String, Integer> stopwords = StopWord.stopwords("C:\\space\\AI\\cluster\\HanLP\\src\\test\\java\\com\\hankcs\\example\\stopwords.txt");
        try (PreparedStatement statement = conn.prepareStatement(UPDATE_SQL)) {
            int count = 0;
            for (Map<String, String> task : tasks) {
                System.out.println(task);
                String cont = task.get("content").toUpperCase();
                count++;
                List<Term> content = segment.seg(cont);
                StringBuilder builder=new StringBuilder();
                content.stream().forEach(w ->{
                    if(!StringUtils.isNullOrEmpty(w.word)&&!stopwords.containsKey(w.word)){
                        builder.append(w.word).append(" ");
                    }
                });
                statement.setString(1, builder.toString());
                statement.setString(2, task.get("id"));
                statement.addBatch();
                if (count % 1000 == 0) {
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            statement.executeBatch();
            statement.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
