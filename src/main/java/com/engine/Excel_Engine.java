package com.engine;

import com.http.MyURI;
import com.http.HttpDriver;
import com.interfaceData.*;
import com.utils.ExcelUtils;
import com.utils.StringUtils;
import com.utils.TestResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口测试引擎，负责遍历 Excel 接口测试用例，
 * 将需要测试的接口所填写的参数全部遍历一次。
 *
 * @version 1.0
 *
 * @since 2017.07.27
 *
 */

public class Excel_Engine {

    private Logger logger = LogManager.getLogger();

    private ExcelUtils excelUtils;
    private ConfigData configData;
    private String excelPath;
    //默认接口测试通过
    private boolean suiteIsPass = true;

    /**
     * 初始化引擎类，提供测试用例文档路径
     *
     * @param excelPath 测试用例文档路径
     *
     */
    public Excel_Engine(final String excelPath) {
        this.excelPath = excelPath;
        this.excelUtils = new ExcelUtils(excelPath);
        this.configData = excelUtils.getInterfaceConfig();
    }

    /**
     * 根据测试文档配置表数据运行测试
     *
     */
    public synchronized void runTest() {
        //接口集合
        List<SuiteConfig> suiteConfigList = configData.getSuiteConfigList();
        HttpDriver driver = new HttpDriver(configData.getInterfaceMainURL());

        for (int i = 0; i < suiteConfigList.size(); i++) {
            suiteIsPass = true;
            SuiteConfig suiteConfig = suiteConfigList.get(i);
            ExcelCellData suiteResult = suiteConfig.getExcelCellData();

            if (suiteConfig.getRunMode().equalsIgnoreCase("YES")) {
                suiteResult = this.testInterface(suiteConfig, driver);
            } else {
                logger.info("接口 [" + suiteConfig.getInterfaceName() + "]" + "不进行测试");
                //测试跳过
                suiteResult.setData(TestResult.SKIP.toString());
            }
            suiteConfigList.get(i).setExcelCellData(suiteResult);
        }
        configData.setSuiteConfigList(suiteConfigList);
        //所有需要测试的接口都测试完毕，下面统一将结果输出到 Excel 的接口测试用例上
        excelUtils.writeSuiteResult(configData);
    }

    /**
     * 单独测试一个接口
     *
     * @param suiteConfig 接口配置数据
     * @param driver http 接口启动器
     *
     * @return 接口测试集合结果
     */
    private synchronized ExcelCellData testInterface(final SuiteConfig suiteConfig, HttpDriver driver) {
        //获取测试接口
        String interfaceName = suiteConfig.getInterfaceName();
        logger.info("准备测试接口：[" + interfaceName + "]");

        InterfaceData interfaceData = excelUtils.getInterfaceData(interfaceName);
        //如果没有接口数据，则无需测试，测试结果失败
        if (interfaceData == null) {
            //先获取测试接口的默认测试结果（默认为空或 true）
            ExcelCellData suiteResult = suiteConfig.getExcelCellData();
            suiteResult.setData(TestResult.FAIL.toString());
            return suiteResult;
        }

        //获取接口的信息、接口参数组合
        InterfaceInfo interfaceInfo = interfaceData.getInterfaceInfo();
        List<InterfaceParameters> interfaceParametersList = interfaceData.getParametersList();
        for (int j = 0; j < interfaceParametersList.size(); j++) {
            //对接口参数组合逐一进行测试
            ExcelWriteDatas writeResult = this.testInterfaceParameter(driver, interfaceInfo, interfaceParametersList.get(j));
            //这样写的目的，是为了将测试结果立即传递到 interfaceParametersList
            interfaceParametersList.get(j).setExcelWriteDatas(writeResult);
        }

        //先获取测试接口的默认测试结果（默认为空或 true）
        ExcelCellData suiteResult = suiteConfig.getExcelCellData();
        //判定接口测试总结果，如果某一组参数测试失败，则判定该接口测试失败
        if (suiteIsPass) {
            suiteResult.setData(TestResult.PASS.toString());
            logger.info("接口 [" + interfaceName + "] 全部参数测试通过");
        } else {
            suiteResult.setData(TestResult.FAIL.toString());
            logger.info("接口 [" + interfaceName + "] 测试失败，详情请查看日志和测试用例文件：[" + excelPath + "]");
        }

        interfaceData.setParametersList(interfaceParametersList);
        //将接口具体的测试结果写入到 Excel 中
        excelUtils.writeInterfaceExpectResult(interfaceData);
        return suiteResult;
    }

    /**
     * 测试接口，使用一种参数组合
     *
     * @param driver http 定位器
     * @param interfaceInfo 接口信息
     * @param interfaceParameters 接口参数组合
     *
     * @return 测试结果
     */
    private synchronized ExcelWriteDatas testInterfaceParameter(HttpDriver driver, InterfaceInfo interfaceInfo, InterfaceParameters interfaceParameters) {
        //获取接口测试参数
        String interfaceName = interfaceInfo.getName();
        ExcelWriteDatas writeResult = interfaceParameters.getExcelWriteDatas();

        MyURI myURI = driver.getURI(interfaceInfo, interfaceParameters);
        //将本次请求的完整地址写入到 Excel 中
        writeResult.getWriteDatasMap().get(InterfaceParameters.FULL_URL).setData(myURI.getUri().toString());
        //发起请求，并返回响应数据
        String response = driver.sendRequestAndGetResponse(myURI);

        //填写实际测试结果
        writeResult.getWriteDatasMap().get(InterfaceParameters.ACTUAL).setData(response);
        //填写是否通过测试
        ExcelCellData isPass = writeResult.getWriteDatasMap().get(InterfaceParameters.IS_PASS);
        //如果接口返回的状态不是200，则发生异常，响应数据为空
        if (response != null) {
            //如果响应的数据匹配预期结果，则判断测试成功
            if (StringUtils.isMatchPart(response, interfaceParameters.getExpectResult())) {
                isPass.setData(TestResult.PASS.toString());
                logger.info("接口 [" + interfaceName + "] 本次测试通过，参数列表：" + interfaceParameters);
            } else {
                isPass.setData(TestResult.FAIL.toString());
                suiteIsPass = false;
                logger.error("接口 [" + interfaceName + "] 本次测试失败，响应数据与预期结果不符合，参数列表：" + interfaceParameters);
            }

        } else {
            //接口响应出错，并当做测试失败
            isPass.setData(TestResult.FAIL.toString());
            suiteIsPass = false;
            logger.error("接口 [" + interfaceName + "] 本次测试失败，响应出错，参数列表：" + interfaceParameters);

        }
        //将测试数据，设置到总体的 InterfaceData 中，跳过中间获取的数据值
        writeResult.getWriteDatasMap().put(InterfaceParameters.IS_PASS, isPass);

        //如果测试结果通过，且提取公共参数单元格的值不为空
        String publicParameters = interfaceParameters.getExtractedPublicParameters();
        if (isPass.getData().equals(TestResult.PASS.toString())
                && publicParameters != null
                && publicParameters != ""
                ) {
            //提取公共参数
            this.setParameters(response, publicParameters);
        }
        return writeResult;
    }

    /**
     * 对接口响应数据，提取公共参数
     *
     * @param response 响应参数
     * @param publicParameters 提取参数的字符串
     *
     */
    private synchronized void setParameters(String response, String publicParameters) {
        String[] splits = StringUtils.getSplitString(publicParameters, "\\,");
        Map<String, String> publicParametersMap = new HashMap<>();
        logger.info("正在提取公共参数：[" + publicParameters + "]");
        //提取公共参数，并写入到【公共参数】sheet 里
        for (int i = 0; i < splits.length; i++) {
            String regex = "\"" + splits[i] + "\":(\"[a-zA-Z0-9_\\-]+\"|[a-zA-Z0-9_\\-]+)";
            String publicParam = StringUtils.getMatchString(response, regex);
            //对匹配的字符串进行处理，去掉 key 值和一些标点符号
            publicParam = publicParam.replaceFirst(splits[i],"");
            publicParam = publicParam.replaceAll("[^a-zA-Z0-9_\\--\\-]+", "");
            publicParametersMap.put(splits[i], publicParam);
        }
        if (publicParametersMap != null) {
            excelUtils.setPublicParameters(publicParametersMap);
        } else {
            logger.error("写入公共参数出错");
        }
    }

}
