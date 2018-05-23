package com.http;

import org.apache.http.HttpResponse;

import java.util.Map;

/**
 * 接口测试启动器接口，方便扩展其他接口测试
 *
 * @version 1.0
 */

public interface InterfaceDriver {

    void setHeader(Map<String, String> headers);

    HttpResponse sendRequest(MyURI myURI);

    String getResponseData(HttpResponse response);

}
