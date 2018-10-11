package com.hankcs.example.meituan.extract;

import com.hankcs.example.util.JDBCUtil;
import com.hankcs.example.meituan.util.HttpUtil;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author jianfei.yin
 * @create 2018-08-10 11:03 PM
 **/
public class Meituan {
    private static final String sql =  "insert into meituan (content,userId,star,resturant) values (?,?,?,?)";
    private static String  url="http://www.meituan.com/meishi/api/poi/getMerchantComment?uuid=6b7530e61f5c47308728.1533912925.1.0.0&platform=1&partner=126&originUrl=http%3A%2F%2Fwww.meituan.com%2Fmeishi%2F4091974%2F&riskLevel=1&optimusCode=1&id=4091974&userId=&offset=|&pageSize=|&sortType=1";

    public static void main(String[] args) {
        String[] resturants={"5396841","52163162","50576755","42030772","40833128","1584738","4091974","50963853",
        "95350621","65783958","74528458","5296487","63903865","3311762","66836","4434691","52800270","4137469","6499811","2524878",
        "6215188","157685033","42984568","6117865","89713637","467232","502269","42578490","384473","41714424","74268107","106563267",
        "1601629","380972","2387964","401274","1046917","3305540","414210","6836298"};
        try (Connection conn = JDBCUtil.getConn("cluster")){

            conn.setAutoCommit(true);
            for(String resturant:resturants){
                System.out.println();
                int page=1;
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                boolean flag = true;
                while (flag){
                    String ss= "http://www.meituan.com/meishi/api/poi/getMerchantComment?uuid=6b7530e61f5c47308728.1533912925.1.0.0&platform=1&partner=126&originUrl=http%3A%2F%2Fwww.meituan.com%2Fmeishi%2F"+resturant+"%2F&riskLevel=1&optimusCode=1&id=4091974&userId=&offset=";
                    ss= ss+String.valueOf(200*(page-1))+"&pageSize="+200+"&sortType=1";

                    HttpUtil.Comments comments = HttpUtil.get(ss);
                    for(HttpUtil.Comment comment:comments.getData().getComments()){
                        if((page+1)*200>comments.getData().getTotal()){
                            flag=false;
                        }
                        if(StringUtil.isNullOrEmpty(comment.getComment())){
                            continue;
                        }
                        preparedStatement.setString(1,comment.getComment().replaceAll(" ","").replaceAll("\n"," "));
                        preparedStatement.setString(2,comment.getUserId());
                        preparedStatement.setInt(3,comment.getStar());
                        preparedStatement.setString(4,resturant);
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                    page++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
