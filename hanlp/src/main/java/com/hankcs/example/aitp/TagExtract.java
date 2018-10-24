package com.hankcs.example.aitp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.Gson;
import com.hankcs.a6.DemoA6Poc;
import com.hankcs.baidu.BaiduTag;
import com.hankcs.baidu.TagItem;
import com.hankcs.example.aitp.domain.Tag;
import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.util.JDBCUtil;

import sun.misc.BASE64Decoder;

public class TagExtract {
    private static String CLOCK_IN="select * from clock_in";

    private static String ACTIVITY_PARTICIPATION="select * from activity_participation";

    private static String COMMENT="select * from comment";

    private static String COMMENT_PIC="select * from comment_pic";

    private static String FITNESS_ACTIVITY="select * from fitness_activity";

    private static String PICS="select * from pics";
    private static String PIN_FAN_ACTIVITY="select * from pin_fan_activity";

    private static String PINFAN_PICS="select * from pinfan_pics";

    private static String WECHAT_USER="select * from wechat_user";
    
    private static String QUESTION="select * from question";
    
    public static final String APP_ID = "14384937";
    
    public static final String API_KEY = "uQEDuMnIAz0bQAOEhBiBgDVc";
    
    public static final String SECRET_KEY = "X3Qc07IFwGEOorekUDWTAv2W7h2SRFXs";
    
    private static final BASE64Decoder decoder = new BASE64Decoder();
    
    private static Map<String,List<String>> superTags = new HashMap<String,List<String>>();

    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("dlife2")) {
            AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            Statement statement = conn.createStatement();
            extractTag(statement,client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static List<String> baiduTagAPI(String titile,String content, AipNlp client) {
        if(titile.equals("")) {
         titile = "content";
        }
        if(content.equals("")) {
         content = "titile";
        }
        content = content +" " +content +" " +content +" " +content +" " +content +" ";
           JSONObject res = client.keyword(titile,content, null);
           Gson gson =new Gson();
           BaiduTag jsonObject = gson.fromJson(res.toString(2), BaiduTag.class);
           List<String> listTag = new ArrayList<String>();
           System.out.println(titile+":"+content);
           System.out.println(res.toString(2));
           if(jsonObject == null || jsonObject.getItems() == null)
           {
        	   return listTag;
           }
           for(TagItem t:jsonObject.getItems()) {
            listTag.add(t.getTag());
           }
           return listTag;
       }
	public static List<String> allTagAPI(String titile,String content, AipNlp client) throws IOException {
    	List<String> listTag =baiduTagAPI(titile, content,client);
    	listTag.addAll(DemoA6Poc.getLabel(content));
		return DemoA6Poc.removeDuplicate(listTag);
    }
    private static void extractTag(Statement statement,AipNlp client) throws SQLException, IOException {
    	Map<String,List<String>> labelMap = new HashMap<String,List<String>>();
        ResultSet pinfan = statement.executeQuery(PIN_FAN_ACTIVITY);
        List<Map<String, String>> pinfanList = JDBCUtil.extract(pinfan);
        for(Map<String,String> map:pinfanList){
			if(labelMap.get(map.get("wechat_user_id")) == null) {
//    			labelMap.put(map.get("wechat_user_id"),  DemoA6Poc.getLabel(map.get("descrption")));
				labelMap.put(map.get("wechat_user_id"), allTagAPI(map.get("activitiy_tile"),map.get("descrption"),client));
    		}else {
    			labelMap.get(map.get("wechat_user_id")).addAll( allTagAPI(map.get("activitiy_tile"),map.get("descrption"),client));
    		}
        }
        
        ResultSet fines = statement.executeQuery(FITNESS_ACTIVITY);
        List<Map<String, String>> finesList = JDBCUtil.extract(fines);
        for(Map<String,String> map:finesList){
			if(labelMap.get(map.get("wechat_user_id")) == null) {
    			labelMap.put(map.get("wechat_user_id"),  allTagAPI(map.get("title"),map.get("descrption"),client));
    		}else {
    			labelMap.get(map.get("wechat_user_id")).addAll( allTagAPI(map.get("title"),map.get("descrption"),client));
    		}
        }
        
//        ResultSet question = statement.executeQuery(QUESTION);
//        List<Map<String, String>> questionList = JDBCUtil.extract(question);
//        for(Map<String,String> map:questionList){
//			if(labelMap.get(map.get("wechat_user_id")) == null && map.get("descrption") != null) {
//    			labelMap.put(map.get("wechat_user_id"),  DemoA6Poc.getLabel(map.get("descrption")));
//    		}else if(map.get("descrption") != null){
//    			labelMap.get(map.get("wechat_user_id")).addAll( DemoA6Poc.getLabel(map.get("descrption")));
//    		}
//        }
        
//        ResultSet comment = statement.executeQuery(COMMENT);
//        List<Map<String, String>> commentList = JDBCUtil.extract(comment);
//        for(Map<String,String> map:commentList){
//			if(labelMap.get(map.get("wechat_user_id")) == null) {
//    			labelMap.put(map.get("wechat_user_id"),  allTagAPI("",map.get("content"),client));
//    		}else {
//    			labelMap.get(map.get("wechat_user_id")).addAll( allTagAPI("",map.get("content"),client));
//    		}
//        }
    	//去重
    	for(String userId:labelMap.keySet()) {
    		DemoA6Poc.removeDuplicate(labelMap.get(userId)); 
    	}
    	
//    	int objId = 1;
//    	Map<String, String> tags = new HashMap<String, String>(); 
    	for(String key:labelMap.keySet()) {
    		for(String tag:labelMap.get(key)) {
//    			if(tags.containsKey(tag))
//    			{
//    				HttpUtil.get("http://localhost:8080/api/tag/by/"+key.replaceAll("\"", "")+"/"+tags.get(tag));
//    			}
				String objectID = HttpUtil.get("http://localhost:8080/api/tag/"+tag);
				if(!StringUtil.isNullOrEmpty(objectID))
				{
					HttpUtil.get("http://localhost:8080/api/tag/by/"+key.replaceAll("\"", "")+"/"+objectID);
				}
				else
				{
					System.out.println(tag+ "是无效的");
//    				tags.put(tag, String.valueOf(objId));
//    	            Tag tagDate = new Tag();
//    	            tagDate.setTitle(tag);
//    	            tagDate.setObjId(String.valueOf(objId));
//    	            HttpUtil.post("Http://localhost:8080/api/tag",tagDate);
//    	            HttpUtil.get("http://localhost:8080/api/tag/by/"+key.replaceAll("\"", "")+"/"+String.valueOf(objId));
//    	            objId++;	
				}
    		}
    	}
    }
}
