package com.interfaceData;

import java.util.List;

/**
 * 存储一个接口的所有数据，
 * 包括：路径、描述、参数名称、调用方法、参数、预期结果、实际测试结果
 *
 * @version 1.0
 */

public class InterfaceData {

    private InterfaceInfo interfaceInfo;
    private List<InterfaceParameters> parametersList;

    /**
     * 声明此类，一次性创建以下接口测试数据
     *
     * @param interfaceInfo 接口基本信息
     * @param parametersList 接口参数组合
     */
    public InterfaceData(InterfaceInfo interfaceInfo, List<InterfaceParameters> parametersList) {
        this.interfaceInfo = interfaceInfo;
        this.parametersList = parametersList;
    }

    public InterfaceInfo getInterfaceInfo() {
        return interfaceInfo;
    }

    public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
        this.interfaceInfo = interfaceInfo;
    }

    public List<InterfaceParameters> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<InterfaceParameters> parametersList) {
        this.parametersList = parametersList;
    }

    @Override
    public String toString() {
        return "InterfaceData{" +
                "interfaceInfo=" + interfaceInfo +
                ", parametersList=" + parametersList +
                '}';
    }
}