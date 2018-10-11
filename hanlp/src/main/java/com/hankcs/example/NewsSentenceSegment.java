package com.hankcs.example;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.mysql.cj.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author jianfei.yin
 * @create 2018-08-04 9:40 AM
 **/
public class NewsSentenceSegment {

    private static final String UPDATE_SQL="update news set segments=? where id=? ";
    private static final String SELECT_SQL="select id, content from news where category='news_game' order by id";
    public static void main(String[] args) {
        try (Connection conn = JDBCUtil.getConn("cluster")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_SQL);
            List<Map<String, String>>  extract= JDBCUtil.extract(resultSet);
            exec(extract);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void customerWord(){
        String[] customeWords={"MDS剑仙","秒选","百里玄策","入榜","新版本","首秀","虐汪","东皇太一","南极土地","","体验服","植物大战僵尸","我的世界","98K","穿越火线","荒野行动","剑仙","王者荣耀","英雄联盟","绝地求生","旅行青蛙","迷你世界","辐射4",
        "第五人格","逆水寒","剑网","LOL","战神","DOTA","刺激战场","吃鸡","梦幻西游","DNF","绝地求生","CF","三国杀","哈希世界","区块链","魔兽争霸3",
        "空调骑士","碧蓝航线","莲花迷宫","不可思议的幻想乡","失落城堡","狂野飙车","业余游玩","天龙八部","炉石传说","QQ飞车","尼尔 机械军团",
        "荒野大镖客","东方梦符祭","魔兽世界","坦克世界","RPG","梦幻西游","阴阳师","东归英雄传","堡垒之夜","麻将","神武",
        "大话西游","剑仙","守望先锋","拳皇","方舟","皇室战争","红色警戒","黎明杀机","三国志","GTA","冰汽时代","地下城与勇士","黄金联赛","S11","S12","S10","AG超玩会"};
        Arrays.stream(customeWords).forEach(w->CustomDictionary.add(w,"nr 10"));
    }

    public static final HashMap<String,String> SYNONYM_MAP = new HashMap();

    static {
        SYNONYM_MAP.put("LOL","英雄联盟");
        SYNONYM_MAP.put("绝地求生","吃鸡");
        SYNONYM_MAP.put("刺激战场","吃鸡");
        SYNONYM_MAP.put("王者农药","王者荣耀");
        SYNONYM_MAP.put("DNF","地下城与勇士");
        SYNONYM_MAP.put("黄金大奖赛","黄金联赛");
        SYNONYM_MAP.put("CF","穿越火线");
        SYNONYM_MAP.put("MDS剑仙","剑仙");
    }


    public static HashMap<String,Integer> stopwords(){
        HashMap<String,Integer> stopwords= new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\space\\AI\\cluster\\HanLP\\src\\test\\java\\com\\hankcs\\example\\stopwords.txt"))) {
            String tmp;
            while ((tmp=reader.readLine())!=null){
                stopwords.put(tmp,null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }
    public static void exec( List<Map<String,String>> tasks){
        Segment segment =new NShortSegment()
            .enablePlaceRecognize(true)
            .enableOrganizationRecognize(false)
            .enableTranslatedNameRecognize(true)
            .enableJapaneseNameRecognize(true)
            .enableNameRecognize(true)
            .enableAllNamedEntityRecognize(true)
            .enableNumberQuantifierRecognize(true)
            .enableCustomDictionary(true)
            .enableMultithreading(10);
        customerWord();

        Connection conn=JDBCUtil.getConn("cluster");

        HashMap<String, Integer> stopwords = stopwords();
        try (PreparedStatement statement = conn.prepareStatement(UPDATE_SQL)) {
            int count = 0;
            for (Map<String, String> task : tasks) {
                System.out.println(task);
                String cont = task.get("content").toUpperCase();
                Set<Map.Entry<String, String>> entries = SYNONYM_MAP.entrySet();
                for(Map.Entry<String, String> entry:entries){
                   cont = cont.replaceAll(entry.getKey(),entry.getValue());
                }
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
