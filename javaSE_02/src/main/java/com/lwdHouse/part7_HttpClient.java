package com.lwdHouse;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * http编程
 */
public class part7_HttpClient {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        httpReqOldVersion();
//        httpReqNewVersion();
//        httpPostReqNewVersion();
    }
    // 老版本的http请求方式
    public static void httpReqOldVersion() throws IOException {
        URL url = new URL("http://www.baidu.com/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 11; Windows NT 5.1)");
        // 连接并发送http请求
        conn.connect();
        if(conn.getResponseCode() >= 400){
            throw new RuntimeException("bad response");
        }
        // 获取所有的响应Header
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key :
                map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
        // 获取响应内容
        InputStream input = conn.getInputStream();
    }
    private static HttpClient httpClient = HttpClient.newBuilder().build();
    // 新版本的请求方式
    public static void httpReqNewVersion() throws URISyntaxException, IOException, InterruptedException {

        String url = "http://www.baidu.com/";
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .header("User-Agent", "Java HttpClient")
                .header("Accept", "*/*")
                .timeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // 如果要获取图片等二进制内容, 可以使用
//        HttpResponse<byte[]> response1 = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
//        byte[] res1 = response1.body();
        // 如果响应内容很大，不希望一次性加载到内存，可以
//        HttpResponse<InputStream> response2 = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
//        InputStream res2 = response2.body();
        Map<String, List<String>> headers = response.headers().map();
        for (String key : headers.keySet()) {
            System.out.println(key + ": " + headers.get(key));
        }
        System.out.println(response.body().substring(0, 1024) + "...");
    }

    // post请求
    public static void httpPostReqNewVersion() throws URISyntaxException, IOException, InterruptedException {
        String url = "http://www.baidu.com";
        String body = "username=123&password=456";
        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .header("Accept",  "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String res = response.body();
    }
}
