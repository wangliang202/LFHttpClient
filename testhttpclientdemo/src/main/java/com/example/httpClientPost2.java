package com.example;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class httpClientPost2 {
    public static final String api_id = "38aced1251254371b7611b7a7f73cdf4";
    public static final String api_secret = "5a37c67d532840ad89f516b61b4d8169";
    public static final String username = "王梁";
    public static final String id_number = "130203198702025153";
    public static final String filepath1="/Users/wangliang/Desktop/db8dc49e55b04070a4f746ddc0260b3c.jpg";//图片1路径
    public static final String filepath2="/Users/wangliang/test.jpg";//图片2路径
    //    public static final String POST_URL = "https://cloudapi.linkface.cn/identity/historical_selfie_verification";
    public static final String POST_URL = "https://cloudapi.linkface.cn/identity/selfie_idnumber_verification";

    public static void HttpClientPost2() throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(POST_URL);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
                .create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("selfie_file", new File(filepath1), ContentType.APPLICATION_OCTET_STREAM, "11111.jpeg");
        HttpEntity reqEntity = entityBuilder
                .setCharset(CharsetUtils.get("UTF-8"))
                .addTextBody("api_id", api_id)
                .addTextBody("api_secret", api_secret)
                .addTextBody("name", username,ContentType.create("text/plain", Charset.forName("UTF-8")))
                .addTextBody("id_number", id_number)
                .build();
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httpPost);
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
        HttpClientPost2();
    }
}