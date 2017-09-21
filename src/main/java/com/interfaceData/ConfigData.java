package com.interfaceData;

import java.util.List;

/**
 * 接口测试配置表的数据类型，
 * 包括：http 协议类型，主 url，测试接口测试结果表
 *
 * @version 1.0
 */

public class ConfigData {

    public static final String CONFIG_SHEET_NAME = "配置";

    private InterfaceMainURL interfaceMainURL;
    private List<SuiteConfig> suiteConfigList;

    public ConfigData() {
    }

    /**
     * 接口参数配置
     *
     * @param interfaceMainURL 接口 url 的主干部分
     * @param suiteConfigList 接口参数的配置列表
     */
    public ConfigData(InterfaceMainURL interfaceMainURL, List<SuiteConfig> suiteConfigList) {
        this.interfaceMainURL = interfaceMainURL;
        this.suiteConfigList = suiteConfigList;
    }

    public InterfaceMainURL getInterfaceMainURL() {
        return interfaceMainURL;
    }

    public void setInterfaceMainURL(InterfaceMainURL interfaceMainURL) {
        this.interfaceMainURL = interfaceMainURL;
    }

    public List<SuiteConfig> getSuiteConfigList() {
        return suiteConfigList;
    }

    public void setSuiteConfigList(List<SuiteConfig> suiteConfigList) {
        this.suiteConfigList = suiteConfigList;
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                "interfaceMainURL=" + interfaceMainURL +
                ", suiteConfigList=" + suiteConfigList +
                '}';
    }
}
