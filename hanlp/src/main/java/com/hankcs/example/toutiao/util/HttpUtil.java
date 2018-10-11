package com.hankcs.example.toutiao.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author jianfei.yin
 * @create 2018-08-08 2:11 PM
 **/
public class HttpUtil {
    public static void main(String[] args) {
        get("https://www.toutiao.com/search_content/?offset=100&format=json&keyword=%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80&autoload=true&count=2&cur_tab=1&from=search_tab");
    }



    public static void post(String type,String name){
        String url="http://localhost:8080/save/"+type+"?name="+name;
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(url);
            client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void post(String url,Map<String,Object> params){
        final List<NameValuePair> pairs = new ArrayList<>();
            params.entrySet().stream().forEach(param-> pairs.add(new BasicNameValuePair(param.getKey(),param.getValue().toString()))
        );
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
            client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static ToutiaoEntity get(String url){
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, "UTF-8");
            System.out.println(s);
            Gson gson = new Gson();
            final ToutiaoEntity toutiaoEntity = gson.fromJson(s, ToutiaoEntity.class);
            return toutiaoEntity;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ToutiaoEntity {
        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        private Integer count;
        private List<Data> data;
        private Integer has_more;

        public Integer getHas_more() {
            return has_more;
        }

        public void setHas_more(Integer has_more) {
            this.has_more = has_more;
        }

        @Override
        public String toString() {
            return "ToutiaoEntity{" +
                "count=" + count +
                ", data=" + data +
                ", has_more=" + has_more +
                '}';
        }
    }
    public static class Data{
        private String title;
        private String item_source_url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getItem_source_url() {
            return item_source_url;
        }

        public void setItem_source_url(String item_source_url) {
            this.item_source_url = item_source_url;
        }

        @Override
        public String toString() {
            return "Data{" +
                "title='" + title + '\'' +
                ", item_source_url='" + item_source_url + '\'' +
                '}';
        }
    }
}
