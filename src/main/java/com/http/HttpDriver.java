package com.http;

import com.interfaceData.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 一个主 URl，一个 HttpDriver
 *
 * @version 1.0
 */

public class HttpDriver implements InterfaceDriver {

    private Logger logger = LogManager.getLogger();

    private final String scheme;
    private final String host;

    private CloseableHttpClient httpClient;

    /**
     * 一个 host 配置一个 HttpDriver
     *
     * @param mainURL 接口协议和主 url 数据
     *
     */
    public HttpDriver(InterfaceMainURL mainURL) {
        //设置 http 协议和接口的主 url，不可变动
        this.scheme = mainURL.getScheme();
        this.host = mainURL.getHost();
    }

    /**
     * 使用指定参数组合一条 用于接口测试的主 url
     *
     * @param interfaceInfo 接口信息
     * @param parameters 参数
     *
     * @return 带指定参数的接口 url
     */
    public MyURI getURI(InterfaceInfo interfaceInfo, InterfaceParameters parameters) {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme(this.scheme)
                .setHost(this.host)
                .setPath(interfaceInfo.getPath());
        List<String> paramNames = interfaceInfo.getParamNames();
        for (int i = 0; i < paramNames.size(); i++) {
            String key = paramNames.get(i);
            String value = parameters.getParameters().get(key);
            uriBuilder.setParameter(key, value);
        }
        MyURI myURI = null;
        try {
            myURI = new MyURI(parameters.getMethod(), uriBuilder.build());
            logger.info("获取到接口的具体地址：" + myURI);
        } catch (URISyntaxException e) {
            logger.error("获取接口地址出错");
            logger.catching(e);
        }
        return myURI;
    }

    /**
     * 获取接口测试的主 url
     *
     * @param uriRequestMethod 接口调用方法
     * @param uri 接口地址
     *
     * @return 带指定参数的接口 url
     */
    public MyURI getURI(String uriRequestMethod, String uri) {
        MyURI myURI = null;
        try {
            myURI = new MyURI(uriRequestMethod, new URIBuilder(uri).build());
            logger.info("获取到接口的具体地址：" + myURI);
        } catch (URISyntaxException e) {
            logger.error("获取接口地址出错");
            logger.catching(e);
        }
        return myURI;
    }

    /**
     * 发送请求，自动判断请求方法
     *
     * @param myURI 带请求方法的 URI
     *
     * @return 响应数据
     */
    @Override
    public HttpResponse sendRequest(MyURI myURI) {
        logger.info("创建网络连接");
        httpClient = HttpClients.createDefault();
        URI uri = myURI.getUri();
        HttpUriRequest request;
        switch (myURI.getMethod()) {
            case HttpDelete.METHOD_NAME :
                request = new HttpDelete(uri);
                break;
            case HttpGet.METHOD_NAME :
                request = new HttpGet(uri);
                break;
            case HttpHead.METHOD_NAME :
                request = new HttpHead(uri);
                break;
            case HttpOptions.METHOD_NAME :
                request = new HttpOptions(uri);
                break;
            case HttpPatch.METHOD_NAME :
                request = new HttpPatch(uri);
                break;
            case HttpPost.METHOD_NAME :
                request = new HttpPost(uri);
                break;
            case HttpPut.METHOD_NAME :
                request = new HttpPut(uri);
                break;
            case HttpTrace.METHOD_NAME :
                request = new HttpTrace(uri);
                break;
            default:
                request = new HttpGet(uri);
                break;
        }
        logger.info("正在发起请求：" + myURI);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            logger.error("发起请求出错，正准备重新请求");
            logger.catching(e);
            //重新请求一次
            try {
                response = httpClient.execute(request);
            } catch (IOException e1) {
                logger.error("再一次发起请求出错，无法得到响应");
                logger.catching(e1);
            }
        }
        return response;
    }

    /**
     * 获取接口响应的数据
     *
     * @param response 接口响应
     *
     * @return 接口响应的数据
     */
    @Override
    public String getResponseData(HttpResponse response) {
        //如果响应数据为空，则是发起请求出错了，响应数据依然为空值
        if (response == null) {
            logger.error("接口响应数据为空");
            return null;
        }

        int statusCode = response.getStatusLine().getStatusCode();
        logger.info("接口返回状态码：[" + statusCode + "]");

        HttpEntity entity = response.getEntity();
        //StringBuilder stringBuilder = new StringBuilder();
        String responseData = null;
        try {
/*            if (entity != null) {
                InputStream inputStream = entity.getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line = null;
                    try {
                        while ((line = reader.readLine())!= null) {
                            stringBuilder.append(line + "\n");
                        }
                    } catch (IOException e) {
                        logger.catching(e);
                    }
                } finally {
                    inputStream.close();
                }
            }
            logger.info("接口响应数据：" + stringBuilder.toString());*/
            responseData = EntityUtils.toString(entity, "UTF-8");
            logger.info("接口响应数据：" + responseData);
        } catch (IOException e) {
            logger.error("获取接口响应数据异常");
            logger.catching(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.info("网络连接关闭异常");
                logger.catching(e);
            }
        }
        return responseData;
    }

    /**
     * 对接口使用指定参数组合发起请求，获取响应数据
     *
     * @param myURI 带参数的 url
     *
     * @return 接口响应数据
     */
    public String sendRequestAndGetResponse(MyURI myURI) {
        return getResponseData(this.sendRequest(myURI));
    }

}