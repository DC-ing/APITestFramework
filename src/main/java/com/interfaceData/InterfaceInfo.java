package com.interfaceData;

import java.util.List;

/**
 * 接口的基本信息，
 * 包括：名称、路径、描述、参数
 *
 * @version 1.0
 */

public class InterfaceInfo {

    private String name;
    private String path;
    private String describe;
    //接口的参数名称
    private List<String> paramNames;

    /**
     * 声明类时，即时创建接口信息
     *
     * @param name 接口名称
     * @param path 接口路径
     * @param describe 接口用途描述
     * @param paramNames 参数列表
     */
    public InterfaceInfo(String name, String path, String describe, List<String> paramNames) {
        this.name = name;
        this.path = path;
        this.describe = describe;
        this.paramNames = paramNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    @Override
    public String toString() {
        return "Interface-\""+ name +"\"{" +
                "path='" + path + '\'' +
                ", describe='" + describe + '\'' +
                ", paramNames=" + paramNames +
                '}';
    }
}

