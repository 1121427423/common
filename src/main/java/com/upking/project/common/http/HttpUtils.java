package com.upking.project.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author king
 * @version 1.0
 * @className HttpUtils
 * @description HTTP工具类
 * @date 2022/7/7
 */
@Slf4j
public class HttpUtils {

    public static String POST = "post";
    private static final String CHARSET = "UTF-8";
    private static String contentType = "application/json";
    private static String charset = "utf-8";
    /**
     * 连接超时 60s
     */
    private static final int CONNECT_TIMEOUT = Integer.parseInt("60000");
    /**
     * 请求超时 60s
     */
    private static final int SO_TIMEOUT = Integer.parseInt("60000");
    private HttpUtils() {}

    public static String doPostWithXML(String protocol, String url, String xml, String xmlCharset) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse resp = null;
        String rtnValue = null;
        try {
            if (protocol.equals("http")) {
                httpClient = HttpClients.createDefault();
            } else {
                // 获取https安全客户端
                httpClient = HttpUtils.getHttpsClient();
            }

            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);

            httpPost.setEntity(new StringEntity(xml, xmlCharset));
            httpPost.setHeader("Content-type", "text/xml");

            resp = httpClient.execute(httpPost);
            rtnValue = EntityUtils.toString(resp.getEntity(), xmlCharset);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("HTTP POST REQUEST FAIL");
        } finally {
            if (null != resp) {
                resp.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }

        return rtnValue;
    }

    /**
     * Post请求
     *
     * @param url
     * @param paraMap
     * @param socketTimeout
     * @param connectTimeout
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> paraMap, String socketTimeout, String connectTimeout)
            throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse resp = null;
        String rtnValue = null;
        try {
            if (url.startsWith("https")) {
                // 获取https安全客户端
                httpClient = HttpUtils.getHttpsClient();
            } else {
                httpClient = HttpClients.createDefault();
            }

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if (null != paraMap && paraMap.size() > 0) {
                for (Map.Entry<String, Object> entry : paraMap.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
            }

            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT)
                    .setConnectTimeout(CONNECT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            resp = httpClient.execute(httpPost);
            rtnValue = EntityUtils.toString(resp.getEntity(), CHARSET);

        } finally {
            if (null != resp) {
                resp.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }

        return rtnValue;
    }

    /**
     * @param @return
     * @param @throws
     *            SanxiaPayBizException
     * @return HttpClient
     * @throws @Method:
     *             getHttpsClient
     * @Description: https 单向验证
     */
    public static CloseableHttpClient getHttpsClient() throws Exception {
        try {
            TrustManager[] trustManagers = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };
            SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslContext.init(new KeyManager[0], trustManagers, new SecureRandom());
            SSLContext.setDefault(sslContext);
            sslContext.init(null, trustManagers, null);
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpClientBuilder clientBuilder = HttpClients.custom().setSSLSocketFactory(connectionSocketFactory);
            clientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
            CloseableHttpClient httpClient = clientBuilder.build();
            return httpClient;
        } catch (Exception e) {
            throw new Exception("http client 远程连接失败", e);
        }
    }

}
