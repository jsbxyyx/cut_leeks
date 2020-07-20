package io.jsbxyyx.cutleek.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;

public class HttpClient {

    private static volatile HttpClient client;
    private static PoolingHttpClientConnectionManager connectionManager;

    public static HttpClient getHttpClient() {
        HttpClient tmp = client;
        if (tmp == null) {
            synchronized (HttpClient.class) {
                tmp = client;
                if (tmp == null) {
                    tmp = new HttpClient();
                    client = tmp;
                }
            }
        }
        return tmp;
    }

    private HttpClient() {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);// 连接池
        connectionManager.setDefaultMaxPerRoute(100);// 每条通道的并发连接数
    }

    private CloseableHttpClient getHttpClient(int connectionTimeout, int socketTimeOut) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionTimeout).setSocketTimeout(socketTimeOut).build();
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    public String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        return getResponseContent(url, httpGet);
    }

    public String post(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        return getResponseContent(url, httpPost);
    }

    private String getResponseContent(String url, HttpRequestBase request) throws Exception {
        HttpResponse response = null;
        try {
            response = this.getHttpClient(2000,2000).execute(request);
            return EntityUtils.toString(response.getEntity(),"utf-8");
        } catch (Exception e) {
            throw new Exception("got an error from HTTP for url : " + URLDecoder.decode(url, "UTF-8"),e);
        } finally {
            if(response != null){
                EntityUtils.consumeQuietly(response.getEntity());
            }
            request.releaseConnection();
        }
    }

}
