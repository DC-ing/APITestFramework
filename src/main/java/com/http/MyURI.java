package com.http;

import java.net.URI;

/**
 * 为了更加适应业务的需要，封装这个类。
 * 此类包括：URL，请求Method
 *
 * @version 1.0
 */

public class MyURI {

    private final String method;
    private final URI uri;

    /**
     * 提供以下两个参数构建 MyURI 类
     *
     * @param method Http方法
     * @param uri 网址
     */
    public MyURI(String method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "MyURI{" +
                "method='" + method + '\'' +
                ", uri=" + uri +
                '}';
    }
}