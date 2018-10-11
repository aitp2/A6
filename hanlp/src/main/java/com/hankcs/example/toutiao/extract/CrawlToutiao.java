package com.hankcs.example.toutiao.extract;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.example.toutiao.util.HttpUtil;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author jianfei.yin
 * @create 2018-08-08 3:50 PM
 **/
public class CrawlToutiao {
    private static final String INSERT_SQL="insert into toutiao (title,url) values (?,?)";
    public static void main(String[] args) {
        boolean stop = false;
        Integer page = 0;
        while (!stop){
            if(crwal(page*20)){
                stop = true;
            }
            page++;
        }
    }
    private static Boolean crwal(Integer offset){
        HttpUtil.ToutiaoEntity toutiaoEntity = HttpUtil.get("https://www.toutiao.com/search_content/?offset="+offset+"&format=json&keyword=%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80&autoload=true&count=20&cur_tab=1&from=search_tab");
        Boolean finish = false;
        if(toutiaoEntity.getHas_more() == 0){
            finish = true;
        }
        if(toutiaoEntity!=null && toutiaoEntity.getData()!=null){
            try (Connection conn = JDBCUtil.getConn("cluster")){
                conn.setAutoCommit(true);
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_SQL);
                for(HttpUtil.Data data: toutiaoEntity.getData()){
                    if(StringUtils.isNullOrEmpty(data.getItem_source_url())||StringUtils.isNullOrEmpty(data.getTitle())){
                        continue;
                    }
                    preparedStatement.setString(1,data.getTitle());
                    preparedStatement.setString(2,"https://www.toutiao.com/"+data.getItem_source_url());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return finish;
    }
}
