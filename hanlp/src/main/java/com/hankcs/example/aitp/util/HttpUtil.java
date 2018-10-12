package com.hankcs.example.aitp.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author jianfei.yin
 * @create 2018-08-27 9:53 PM
 **/
public class HttpUtil {
    public static void post(String url,Object entity){
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-type","application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            Gson gson =new Gson();
            StringEntity se = new StringEntity(gson.toJson(entity),"UTF-8");
//            se.setContentType("application/json");
//            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String get(String url){
    	String result = null;
        try (CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get= new HttpGet(url);
            CloseableHttpResponse response =client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
