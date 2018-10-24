package com.hankcs.example.aitp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import com.baidu.aip.nlp.AipNlp;
import com.google.gson.Gson;
import com.hankcs.a6.DemoA6Poc;
import com.hankcs.baidu.BaiduTag;
import com.hankcs.baidu.TagItem;
import com.hankcs.example.aitp.data.KeywordsCoefficientdata;
import com.hankcs.example.aitp.data.KeywordsEmployeedata;
import com.hankcs.example.aitp.domain.Tag;
import com.hankcs.example.aitp.util.HttpUtil;
import com.hankcs.example.meituan.util.HttpUtil.Comments;
import com.hankcs.example.util.JDBCUtil;

import sun.misc.BASE64Decoder;

public class CoefficientExtract {
	private static final String sql =  "insert into coefficient (custmerID,keywords,publishActive,attendActive,commentActive,commentAttendScale,checkinAttendScale,totalActive,feelingActive) values (?,?,?,?,?,?,?,?,?)";
	
    public static final String APP_ID = "14384937";
    
    public static final String API_KEY = "uQEDuMnIAz0bQAOEhBiBgDVc";
    
    public static final String SECRET_KEY = "X3Qc07IFwGEOorekUDWTAv2W7h2SRFXs";
    
    private static final BASE64Decoder decoder = new BASE64Decoder();
    
    private static Map<String,List<String>> superTags = new HashMap<String,List<String>>();

    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("a6")) {
            AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);
            PreparedStatement statement = conn.prepareStatement(sql);
            extractCoefficient(statement,client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractCoefficient(PreparedStatement statement,AipNlp client) throws SQLException, IOException {
    	
    	Map<String,List<String>> superTags = new HashMap<String,List<String>>();
    	superTags.put("饮料", Arrays.asList(new String[]{"椰肉","椰汁","鲜奶","奶茶","椰子","夏日清","红茶","茶","喝酒","眷村","咖啡","星冰乐","果蔬汁"}));
    	superTags.put("食物", Arrays.asList(new String[]{"饮料","糖","芋头","冰无糖","芋","紫米","早饭","海盐","火锅","焦糖","主食","面","雪糕","炒米粉","果冻","凉粉","啤酒鸭","火龙果","豆腐","香草","小食","鱼蛋","龟苓膏","螃蟹","奶昔","海鲜"}));
    	superTags.put("宠物", Arrays.asList(new String[]{"狗","边牧","猫","鸡","海鸥","云吸宠","天鹅"}));
    	superTags.put("建筑", Arrays.asList(new String[]{"寺庙","庙"}));
    	superTags.put("体育", Arrays.asList(new String[]{"看球","跳伞","篮球","平板","球衣","台球","球衣","健身房","蹦床"}));
    	superTags.put("娱乐", Arrays.asList(new String[]{"跳伞","电视","音乐","逛街","电影","书","露营","旅途","杀人","蹦床"}));
    	superTags.put("音乐", Arrays.asList(new String[]{"二胡","歌曲","歌","乐器","歌单","单曲","首歌"}));
    	
    	 for (Map.Entry<String,List<String>> entry : superTags.entrySet()) {
			for(String subTagTitle: entry.getValue())
			{
				String s = HttpUtil.get("http://localhost:8080/api/getCoefficientDataByTag/"+subTagTitle);
	            Gson gson = new Gson();
	            final KeywordsCoefficientdata keywordsCoefficientdata = gson.fromJson(s, KeywordsCoefficientdata.class);
	 		   if(!CollectionUtils.isEmpty(keywordsCoefficientdata.getKeywordsEmployeedatas()))
			   {
				   for(KeywordsEmployeedata keywordsEmployeedata : keywordsCoefficientdata.getKeywordsEmployeedatas())
				   {
					   //save KeywordsCoefficientdata
					   statement.setInt(1, Integer.valueOf(keywordsEmployeedata.getEmployeeData().getObjId()));
					   statement.setString(2, subTagTitle);
					   statement.setDouble(3, keywordsEmployeedata.getPublishActive());
					   statement.setDouble(4, keywordsEmployeedata.getAttendActive());
					   statement.setDouble(5, keywordsEmployeedata.getCommentActive());
					   statement.setDouble(6, keywordsEmployeedata.getCommentAttendScale());
					   statement.setDouble(7, keywordsEmployeedata.getCheckinAttendScale());
					   statement.setDouble(8, keywordsEmployeedata.getTotalActive());
					   statement.setDouble(9, keywordsEmployeedata.getFeelingActive());
					   statement.addBatch();
				   }
				   statement.executeBatch();
				   statement.clearBatch();
			   }
			}
    	}
    }
}
