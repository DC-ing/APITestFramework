package com.interfaceData;

import org.apache.http.client.methods.*;

import java.util.Map;

/**
 * 接口调用方式、参数、预期结果、实际测试结果的数据类.
 * PS: 一个接口测试参数组合
 *
 * @version 1.0
 */

public class InterfaceParameters {

    public static final String FULL_URL = "完整URL";
    public static final String EXPECT = "预期结果";
    public static final String ACTUAL = "实际测试结果";
    public static final String IS_PASS = "是否通过测试";
    public static final String EXTRACTED_PUBLIC_PARAMETERS = "提取公共参数";

    //接口的调用方式，一般有 GET、POST 等
    private String method;
    //接口的参数数据
    private Map<String, String> parameters;
    //预期接口返回的字段或数据，可用于断言
    private String expectResult;

    public String extractedPublicParameters;
    //所有需要写入的接口相关数据
    private ExcelWriteDatas excelWriteDatas;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        switch (method.toUpperCase()) {
            case HttpDelete.METHOD_NAME :
                this.method = HttpDelete.METHOD_NAME;
                break;
            case HttpGet.METHOD_NAME :
                this.method = HttpGet.METHOD_NAME;
                break;
            case HttpHead.METHOD_NAME :
                this.method = HttpHead.METHOD_NAME;
                break;
            case HttpOptions.METHOD_NAME :
                this.method = HttpOptions.METHOD_NAME;
                break;
            case HttpPatch.METHOD_NAME :
                this.method = HttpPatch.METHOD_NAME;
                break;
            case HttpPost.METHOD_NAME :
                this.method = HttpPost.METHOD_NAME;
                break;
            case HttpPut.METHOD_NAME :
                this.method = HttpPut.METHOD_NAME;
                break;
            case HttpTrace.METHOD_NAME :
                this.method = HttpTrace.METHOD_NAME;
                break;
            case "CONNECT" :
                this.method = "CONNECT";
                break;
            default:
                this.method = method;
                break;
        }
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getExpectResult() {
        return expectResult;
    }

    public void setExpectResult(String expectResult) {
        this.expectResult = expectResult;
    }

    public String getExtractedPublicParameters() {
        return extractedPublicParameters;
    }

    public void setExtractedPublicParameters(String extractedPublicParameters) {
        this.extractedPublicParameters = extractedPublicParameters;
    }

    public ExcelWriteDatas getExcelWriteDatas() {
        return excelWriteDatas;
    }

    public void setExcelWriteDatas(ExcelWriteDatas excelWriteDatas) {
        this.excelWriteDatas = excelWriteDatas;
    }

    @Override
    public String toString() {
        return "InterfaceParameters{" +
                "method='" + method + '\'' +
                ", parameters=" + parameters +
                ", expectResult='" + expectResult + '\'' +
                "," + excelWriteDatas +
                '}';
    }
}
