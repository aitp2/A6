package com.hankcs.example.aitp;

import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jianfei.yin
 * @create 2018-08-30 9:44 PM
 **/
public class Similarity {


    private static String ACTIVITY_PARTICIPATION="select * from activity_participation";


    private static String ATTENDEE="select * from attendee";

    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("aitp2")) {
            Statement statement = conn.createStatement();

            ResultSet attendeeSet = statement.executeQuery(ATTENDEE);
            List<Map<String, String>> attendees = JDBCUtil.extract(attendeeSet);
            ResultSet participationSet = statement.executeQuery(ACTIVITY_PARTICIPATION);
            List<Map<String, String>> parts = JDBCUtil.extract(participationSet);

            Map<String, List<Map<String, String>>> attendeeMap = attendees.stream().collect(Collectors.groupingBy(a -> a.get("wechat_user_id")));
            Map<String, List<Map<String, String>>> partsMap = parts.stream().collect(Collectors.groupingBy(p -> p.get("wechat_user_id")));

            Map<String,Set<AttendedActivity>> map1= new HashMap<>();
            Iterator<Map.Entry<String, List<Map<String, String>>>> iterator = attendeeMap.entrySet().iterator();
            Iterator<Map.Entry<String, List<Map<String, String>>>> iterator1 = partsMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, List<Map<String, String>>> next = iterator.next();
                Set<AttendedActivity> set=new HashSet<>();
                List<Map<String, String>> value = next.getValue();
                value.forEach(v->{
                    set.add(toAttendedActivity(v,"pin"));
                });
                map1.put(next.getKey(),set);
            }
            while (iterator1.hasNext()){
                Map.Entry<String, List<Map<String, String>>> next = iterator1.next();
                Set<AttendedActivity> set=map1.containsKey(next.getKey())?map1.get(next.getKey()):new HashSet<>();
                List<Map<String, String>> value = next.getValue();
                value.forEach(v->{
                    set.add(toAttendedActivity(v,"fit"));
                });
                map1.put(next.getKey(),set);
            }
            Iterator<Map.Entry<String, Set<AttendedActivity>>> iterator2 = map1.entrySet().iterator();
            iterator2.forEachRemaining((Map.Entry<String, Set<AttendedActivity>> next) ->{
                Iterator<Map.Entry<String, Set<AttendedActivity>>> iterator3 = map1.entrySet().iterator();
                iterator3.forEachRemaining((Map.Entry<String, Set<AttendedActivity>> next2) ->{
                    if(!next.getKey().equals(next2.getKey())){
                        Set<AttendedActivity> intersection = new HashSet<>();
                        intersection.addAll(next.getValue());
                        intersection.retainAll(next2.getValue());
                        Set<AttendedActivity> union=new HashSet<>();
                        union.addAll(next.getValue());
                        union.addAll(next2.getValue());
                        if(intersection.size()>0&&union.size()>0){
                            BigDecimal divide = new BigDecimal(intersection.size()).divide(new BigDecimal(union.size()), 5, BigDecimal.ROUND_UP);
                            HttpUtil.get("http://localhost:8080/api/similar/"+next.getKey()+"/"+next2.getKey()+"/"+divide);
                        }
                    }
                });
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static AttendedActivity toAttendedActivity(Map<String,String> map,String type){
        String id;
        if("pin".equals(type)){
            id = map.get("pin_fan_activity_id");
        }else {
            id = map.get("fitness_activity_id");
        }
       String userId = map.get("wechat_user_id");
       return new AttendedActivity(type,id,userId);
    }
    private static class AttendedActivity{
        private String type;
        private String id;
        private String userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AttendedActivity)) return false;

            AttendedActivity that = (AttendedActivity) o;

            if (!getType().equals(that.getType())) return false;
            return getId().equals(that.getId());
        }

        @Override
        public int hashCode() {
            int result = getType().hashCode();
            result = 31 * result + getId().hashCode();
            return result;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public AttendedActivity(String type, String id, String userId) {
            this.type = type;
            this.id = id;
            this.userId = userId;
        }





    }


}
