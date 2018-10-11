package com.hankcs.example.aitp;

import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author jianfei.yin
 * @create 2018-08-27 11:42 PM
 **/
public class RelationshipExtract {
    private static String CLOCK_IN="select * from clock_in";

    private static String ACTIVITY_PARTICIPATION="select * from activity_participation";

    private static String COMMENT="select * from comment";

    private static String COMMENT_PIC="select * from comment_pic";

    private static String FITNESS_ACTIVITY="select * from fitness_activity";

    private static String PICS="select * from pics";
    private static String PIN_FAN_ACTIVITY="select * from pin_fan_activity";

    private static String PINFAN_PICS="select * from pinfan_pics";

    private static String WECHAT_USER="select * from wechat_user";

    private static String ATTENDEE="select * from attendee";


    private static final BASE64Decoder decoder = new BASE64Decoder();

    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("dlife2")) {
            Statement statement = conn.createStatement();
            extractLaunch(statement);
            extractEdrect(statement);
            extractCommentBy(statement);
            extractComment(statement);
            extractInvitationAttended(statement);
            extractObjectiveAttended(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void extractCommentBy(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(COMMENT);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            HttpUtil.get("http://localhost:8080/api/comment/by/"+map.get("wechat_user_id")+"/"+map.get("id"));
        }
    }

    private static void extractComment(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(COMMENT);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            if("FIT".equals(map.get("channel"))){
                HttpUtil.get("http://localhost:8080/api/comment/objective/"+map.get("object_id")+"/"+map.get("id"));
            }else{
                HttpUtil.get("http://localhost:8080/api/comment/invitation/"+map.get("object_id")+"/"+map.get("id"));
            }
        }
    }
    private static void extractInvitationAttended(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(ATTENDEE);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            HttpUtil.get("http://localhost:8080/api/attended/invitation/"+map.get("wechat_user_id")+"/"+map.get("pin_fan_activity_id"));
        }
    }
    private static void extractObjectiveAttended(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(ACTIVITY_PARTICIPATION);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            HttpUtil.get("http://localhost:8080/api/attended/objective/"+map.get("wechat_user_id")+"/"+map.get("fitness_activity_id"));
        }
    }
    private static void extractLaunch(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(PIN_FAN_ACTIVITY);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            HttpUtil.get("http://localhost:8080/api/launch/"+map.get("wechat_user_id")+"/"+map.get("id"));
        }
    }
    private static void extractEdrect(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(FITNESS_ACTIVITY);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            HttpUtil.get("http://localhost:8080/api/edrect/"+map.get("wechat_user_id")+"/"+map.get("id"));
        }
    }
    private static String decode(String origin) throws IOException {
        if(StringUtil.isNullOrEmpty(origin)){
            return "";
        }
        return new String(decoder.decodeBuffer(origin),"UTF-8");
    }
}
