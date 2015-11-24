package com.piggsoft.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2015/11/16.
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static int timeout = 10 * 1000;

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final RequestConfig CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(timeout).setConnectTimeout(timeout)
            .setSocketTimeout(timeout).build();

    private static ResponseHandler responseHandler = new ResponseHandler();

    public static String post(String url, Map<String, Object> params) {
        return post(url, DEFAULT_ENCODING, params, null);
    }

    public static String post(String url, String content) {
        return post(url, DEFAULT_ENCODING, null, content);
    }

    public static String post(String url, String charset, Map<String, Object> params, String content) {
        return _post(url, charset, params, content);
    }

    private static String _post(String url, String charset, Map<String, Object> params, String content) throws HttpException {
        CloseableHttpClient httpClient = null;
        HttpPost post = null;
        String responseMessage = null;
        try {
            httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            post.setConfig(CONFIG);
            HttpEntity httpEntity = getEntity(params, content, charset);
            post.setEntity(httpEntity);
            //不能加上Content-Type,加上后台servlet不会解析参数
            //post.setHeader("Content-Type", "text/xml;charset=" + charset);
            /*CloseableHttpResponse response = httpClient.execute(post);
            responseMessage = EntityUtils.toString(response.getEntity(), Charset.forName(charset));*/
            responseMessage = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
            throw new HttpException(e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpException(e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new HttpException(e);
                }
            }
        }
        return responseMessage;
    }

    private static HttpEntity getEntity(Map<String, Object> params, String content, String charset) throws UnsupportedEncodingException {
        if (params != null && !params.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("请求：params({});chartset({})", new String[]{JSON.toJSONString(params), charset});
            }
            List<NameValuePair> _params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                _params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            return new UrlEncodedFormEntity(_params, charset);
        }
        if (StringUtils.isNotEmpty(content)) {
            if (logger.isDebugEnabled()) {
                logger.debug("请求：params({});chartset({})", new String[]{content, charset});
            }
            return new StringEntity(content, ContentType.create("application/x-www-form-urlencoded", Charset.forName(charset)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("请求：没有参数");
        }
        return new StringEntity("");
    }

    public static String get(String url, Map<String, Object> params) {
        return get(url, params, DEFAULT_ENCODING);
    }

    public static String get(String url, Map<String, Object> params, String encoding) {
        CloseableHttpClient httpClient = null;
        HttpUriRequest get = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();
            RequestBuilder requestBuilder = RequestBuilder.get();
            requestBuilder.setUri(url);
            requestBuilder.setHeader("Content-Type", "text/xml;charset=" + encoding);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
            get = requestBuilder.build();
            if (logger.isDebugEnabled()) {
                logger.debug("Send Request : {}", get.getRequestLine().toString());
            }
            /*CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, encoding);*/
            result = httpClient.execute(get, responseHandler);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    public static String postChunk(String url, File file) {
        CloseableHttpClient httpClient = null;
        HttpPost post = null;
        String responseMessage = null;
        try {
            httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            post.setConfig(CONFIG);
            InputStreamEntity httpEntity = new InputStreamEntity(
                    new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
            post.setEntity(httpEntity);
            //不能加上Content-Type,加上后台servlet不会解析参数
            //post.setHeader("Content-Type", "text/xml;charset=" + charset);
            /*CloseableHttpResponse response = httpClient.execute(post);
            responseMessage = EntityUtils.toString(response.getEntity(), Charset.forName(charset));*/
            responseMessage = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage(), e);
            throw new HttpException(e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new HttpException(e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new HttpException(e);
                }
            }
        }
        return responseMessage;
    }

    static class ResponseHandler implements org.apache.http.client.ResponseHandler<String> {
        public String handleResponse(HttpResponse response) throws IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }
    }

}
