package com.hankcs.example.meituan.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author jianfei.yin
 * @create 2018-08-08 2:11 PM
 **/
public class HttpUtil {
    public static void main(String[] args) {
        get("http://www.meituan.com/meishi/api/poi/getMerchantComment?uuid=6b7530e61f5c47308728.1533912925.1.0.0&platform=1&partner=126&originUrl=http%3A%2F%2Fwww.meituan.com%2Fmeishi%2F4091974%2F&riskLevel=1&optimusCode=1&id=4091974&userId=&offset=10&pageSize=1&sortType=1");
    }

    public static Comments get(String url){
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36"); // 设置请求头消息User-Agent
            httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, "UTF-8");
//            System.out.println(s);
            Gson gson = new Gson();
            final Comments comments = gson.fromJson(s, Comments.class);
//            System.out.println(comments);
            return comments;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Comments {
        private Integer status;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
        private Data data;
        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Comments{" +
                "status=" + status +
                ", data=" + data +
                '}';
        }
    }
    public static class Data{
        private Integer total;
        private List<Comment> comments;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "Data{" +
                "total=" + total +
                ", comments=" + comments +
                '}';
        }
    }
    public static class Comment {
       private String comment;
       private String userId;
       private Integer star;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getStar() {
            return star;
        }

        public void setStar(Integer star) {
            this.star = star;
        }

        @Override
        public String toString() {
            return "Comment{" +
                "comment='" + comment + '\'' +
                ", userId='" + userId + '\'' +
                ", star=" + star +
                '}';
        }
    }
}
