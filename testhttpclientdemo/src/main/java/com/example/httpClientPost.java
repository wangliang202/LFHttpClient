package com.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class httpClientPost {
    public static final String api_id = "38aced1251254371b7611b7a7f73cdf4";
    public static final String api_secret = "5a37c67d532840ad89f516b61b4d8169";
    public static final String filepath1="/Users/wangliang/Desktop/WechatIMG393.jpeg";//图片1路径
    public static final String filepath2="/Users/wangliang/test.jpg";//图片2路径
    public static final String POST_URL = "https://cloudapi.linkface.cn/identity/historical_selfie_verification";

    public static void HttpClientPost() throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(POST_URL);
        StringBody id = new StringBody(api_id);
        StringBody secret = new StringBody(api_secret);
        FileBody fileBody1 = new FileBody(new File(filepath1));
        FileBody fileBody2 = new FileBody(new File(filepath2));
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("selfie_file", fileBody1);
        entity.addPart("historical_selfie_file", fileBody2);
        entity.addPart("api_id", id);
        entity.addPart("api_secret", secret);
        entity.addPart("selfie_auto_rotate", new StringBody("true"));
        entity.addPart("historical_selfie_auto_rotate", new StringBody("true"));
        post.setEntity(entity);

        HttpResponse response = httpclient.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entitys = response.getEntity();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(entitys.getContent()));
            String line = reader.readLine();
            System.out.println(line);
        }else{
            HttpEntity r_entity = response.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            System.out.println("错误码是："+response.getStatusLine().getStatusCode()+"  "+response.getStatusLine().getReasonPhrase());
            System.out.println("出错原因是："+responseString);
            //你需要根据出错的原因判断错误信息，并修改
        }

        httpclient.getConnectionManager().shutdown();
    }


    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpClientPost();
    }
}