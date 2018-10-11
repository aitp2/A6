package com.hankcs.example.aitp;

import com.hankcs.example.aitp.domain.*;
import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @author jianfei.yin
 * @create 2018-08-27 10:03 PM
 **/
public class EntityExtract {
    private static String CLOCK_IN="select * from clock_in";

    private static String ACTIVITY_PARTICIPATION="select * from activity_participation";

    private static String COMMENT="select * from comment";

    private static String COMMENT_PIC="select * from comment_pic";

    private static String FITNESS_ACTIVITY="select * from fitness_activity";

    private static String PICS="select * from pics";
    private static String PIN_FAN_ACTIVITY="select * from pin_fan_activity";

    private static String PINFAN_PICS="select * from pinfan_pics";

    private static String WECHAT_USER="select * from wechat_user";

    private static final BASE64Decoder decoder = new BASE64Decoder();

    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("dlife2")) {
            Statement statement = conn.createStatement();
            extractClockIn(statement);
            extractComment(statement);
            extractCOMMENT_PIC(statement);
            extractFITNESS_ACTIVITY(statement);
            extractPIN_FAN_ACTIVITY(statement);
            extractWECHAT_USER(statement);
            extractPics(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String decode(String origin) throws IOException {
        if(StringUtil.isNullOrEmpty(origin)){
            return "";
        }
        return new String(decoder.decodeBuffer(origin),"UTF-8");
    }
    private static void extractPIN_FAN_ACTIVITY(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(PIN_FAN_ACTIVITY);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            SmallInvitation smallInvitation = new SmallInvitation();
            smallInvitation.setDescription(map.get("descrption"));
            smallInvitation.setBudget(StringUtil.isNullOrEmpty(map.get("budget"))?BigDecimal.ZERO:new BigDecimal(map.get("budget")));
            smallInvitation.setAddress(map.get("activitiy_addre"));
            smallInvitation.setComment(map.get("comment"));
            smallInvitation.setReadingCount(Integer.parseInt(map.get("reading_count")));
            smallInvitation.setTitle(map.get("activitiy_tile"));
            smallInvitation.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/smallInvitation",smallInvitation);
        }
    }
    private static void extractFITNESS_ACTIVITY(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(FITNESS_ACTIVITY);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            SmallObjective smallObjective = new SmallObjective();
            smallObjective.setDescription(map.get("descrption"));
//            smallObjective.setEnd(Instant.parse(map.get("activity_end_time")));
//            smallObjective.setStart(Instant.parse(map.get("activity_start_time")));
            smallObjective.setReadingCount(Integer.parseInt(map.get("reading_count")));
            smallObjective.setTitle(map.get("title"));
            smallObjective.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/smallObjective",smallObjective);
        }
    }
    private static void extractComment(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(COMMENT);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            Comment comment = new Comment();
            comment.setContent(map.get("content"));
            comment.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/comment",comment);
        }
    }
    private static void extractCOMMENT_PIC(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(COMMENT_PIC);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        postImage(resultList);
    }
    private static void extractPics(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(PICS);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        postImage(resultList);
    }
    private static void postImage(List<Map<String, String>> resultList){
        for(Map<String,String> map:resultList){
            Image image = new Image();
            image.setPath(map.get("oss_path"));
            image.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/image",image);
        }
    }
    private static void extractClockIn(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(CLOCK_IN);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            ClockIn clockIn = new ClockIn();
            clockIn.setNote(map.get("sign_note"));
            clockIn.setTitle(map.get("title"));
            clockIn.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/clock",clockIn);
        }
    }
    private static void extractWECHAT_USER(Statement statement) throws SQLException, IOException {
        ResultSet resultSet = statement.executeQuery(WECHAT_USER);
        List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);
        for(Map<String,String> map:resultList){
            Employee employee = new Employee();
            employee.setName(decode(map.get("nick_name")));
            employee.setAvatar(map.get("avatar"));
            employee.setGender(map.get("sex"));
            employee.setObjId(map.get("id"));
            HttpUtil.post("Http://localhost:8080/api/employee" +
                "",employee);
        }
    }


}
