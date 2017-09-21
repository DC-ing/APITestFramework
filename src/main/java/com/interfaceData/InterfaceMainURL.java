package com.interfaceData;

/**
 * 接口的协议和域名部分
 *
 * @version 1.0
 */

public class InterfaceMainURL {

    private String scheme;
    private String host;

    /**
     * 声明就要提供 http 协议和域名
     *
     * @param scheme http 协议
     * @param host 域名
     */
    public InterfaceMainURL(String scheme, String host) {
        this.scheme = scheme;
        this.host = host;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "InterfaceMainURL{\"" + scheme + "://" + host + "\"}";
    }
}
