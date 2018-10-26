package com.hankcs.a6;

import com.hankcs.example.aitp.domain.Tag;
import com.hankcs.example.toutiao.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author jianfei.yin
 * @create 2018-10-24 10:25 AM
 **/
public class Tag4Coefficient {

    private static final String POST_UTL ="http://localhost:8080/api/CreateL2Tag";
    private static final String SELECT_FROM_COEFFICIENT = "select * from coefficient";
    public static void main(String[] args) {
        try (Statement statement= JDBCUtil.getConn("a6").createStatement()){
            ResultSet resultSet = statement.executeQuery(SELECT_FROM_COEFFICIENT);
            List<Map<String, String>> resultList = JDBCUtil.extract(resultSet);

            resultList.forEach(r->{
                String custmerID = r.get("custmerID");
                String keywords = r.get("keywords");

                String publishActive = r.get("publishActive");
                HttpUtil.post(POST_UTL,PublishActive.getTag(keywords,Double.valueOf(publishActive),custmerID).toMap());
                String attendActive = r.get("attendActive");
                HttpUtil.post(POST_UTL,AttendActive.getTag(keywords,Double.valueOf(attendActive),custmerID).toMap());
                String commentActive = r.get("commentActive");
                HttpUtil.post(POST_UTL,CommentActive.getTag(keywords,Double.valueOf(commentActive),custmerID).toMap());
                String commentAttendScale = r.get("commentAttendScale");
                HttpUtil.post(POST_UTL, CommentAttendScale.getTag(keywords,Double.valueOf(commentAttendScale),custmerID).toMap());
                String checkinAttendScale = r.get("checkinAttendScale");
                HttpUtil.post(POST_UTL, CheckinAttendScale.getTag(keywords,Double.valueOf(checkinAttendScale),custmerID).toMap());
                String totalActive = r.get("totalActive");
                HttpUtil.post(POST_UTL,TotalActive.getTag(keywords,Double.valueOf(totalActive),custmerID).toMap());
                String feelingActive = r.get("feelingActive");
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Tag convertTag(TaggingDTO dto)
    {
    	Tag tag = new Tag();
    	tag.setLevel("2");
    	tag.setTitle(dto.tag);
    	tag.setObjId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
		return tag;
    	
    }
    public static class TaggingDTO {
        private String custmerId;
        private String tag;

        public TaggingDTO(String custmerId, String tag) {
            this.custmerId = custmerId;
            this.tag = tag;
        }
        public Map<String,Object> toMap(){
            Map<String,Object> map = new HashMap<>();
            map.put("custmerId",this.custmerId);
            map.put("tag",this.tag);
            return map;
        }
    }

    public static class PublishActive {
        private static final Double L1 = 0.3;
        private static final Double L2 = 0.7;
        public static TaggingDTO getTag(String key, Double d, String customerId){
            if (d.compareTo(L1)<=0){
                return new TaggingDTO(customerId, key+"跟随者");
            }else if(d.compareTo(L1)>0 && d.compareTo(L2)<=0){
               return new TaggingDTO(customerId, key+"组织者");
            }else {
               return new TaggingDTO(customerId, key+"领导者");
            }
        }
    }
    public static class AttendActive {
        private static final Double L1 = 0.3;
        private static final Double L2 = 0.7;
        public static TaggingDTO getTag(String key, Double d, String customerId){
            if (d.compareTo(L1)<=0){
                return new TaggingDTO(customerId, key+"打酱油者");
            }else if(d.compareTo(L1)>0 && d.compareTo(L2)<=0){
                return new TaggingDTO(customerId, key+"活动参与者");
            }else {
                return new TaggingDTO(customerId, key+"积极参与者");
            }
        }
    }
    public static class CommentActive {
        private static final Double L1 = 0.3;
        private static final Double L2 = 0.7;
        public static TaggingDTO getTag(String key, Double d, String customerId){
            if (d.compareTo(L1)<=0){
                return new TaggingDTO(customerId, key+"话题沉默者");
            }else if(d.compareTo(L1)>0 && d.compareTo(L2)<=0){
                return new TaggingDTO(customerId, key+"话题参与讨论者");
            }else {
                return new TaggingDTO(customerId, key+"话题活跃者");
            }
        }
    }
    public static class TotalActive {
        private static final Double L1 = 0.3;
        private static final Double L2 = 0.7;

        public static TaggingDTO getTag(String key, Double d, String customerId) {
            if (d.compareTo(L1) <= 0) {
                return new TaggingDTO(customerId, key + "新手");
            } else if (d.compareTo(L1) > 0 && d.compareTo(L2) <= 0) {
                return new TaggingDTO(customerId, key + "普通人");
            } else {
                return new TaggingDTO(customerId, key + "达人");
            }
        }
    }
    public static class CommentAttendScale {
        private static final Double L1 = 0.5;
        private static final Double L2 = 2D;

        public static TaggingDTO getTag(String key, Double d, String customerId) {
            if (d.compareTo(L1) <= 0) {
                return new TaggingDTO(customerId, key+"实干者");
            } else if (d.compareTo(L1) > 0 && d.compareTo(L2) <= 0) {
                return new TaggingDTO(customerId, key+"评论参与均衡掌握人士");
            } else {
                return new TaggingDTO(customerId, key+"评论家");
            }
        }
    }

    public static class CheckinAttendScale {
        private static final Double L1 = 0.5;
        private static final Double L2 = 2D;

        public static TaggingDTO getTag(String key, Double d, String customerId) {
            if (d.compareTo(L1) <= 0) {
                return new TaggingDTO(customerId, key+"名义参与者");
            } else if (d.compareTo(L1) > 0 && d.compareTo(L2) <= 0) {
                return new TaggingDTO(customerId, key+"实际参与者");
            } else {
                return new TaggingDTO(customerId, key+"热心参与者");
            }
        }
    }

}
