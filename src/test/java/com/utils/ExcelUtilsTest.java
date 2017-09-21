package com.utils;

import com.interfaceData.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtilsTest {

    private ExcelUtils excelUtils;
    private String testSheet;

    @BeforeClass
    public void beforeClass() {
        String excelPath = System.getProperty("user.dir") + File.separator + "test-datas" + File.separator + "interface-test-demo.xlsx";
        testSheet = "家长版登录";
        excelUtils = new ExcelUtils(excelPath);
    }

    @Test
    public void test_getExcelType() {
//        System.err.println(excelUtils.getExcelType());
        Assert.assertEquals(excelUtils.getExcelType(), ExcelUtils.XLSX);
    }

    @Test
    public void test_getSheetNames() {
        Assert.assertNotNull(excelUtils.getSheetNames());
    }

    @Test
    public void test_isSheetExist() {
        Assert.assertTrue(excelUtils.isSheetExist(testSheet));
    }

    @Test
    public void test_InterData() {
        InterfaceData data = excelUtils.getInterfaceData(testSheet);
//        System.err.println(data);
        Assert.assertNotNull(data);
    }

    @Test
    public void test_ConfigData() {
        ConfigData data = excelUtils.getInterfaceConfig();
//        System.err.println(data);
        Assert.assertNotNull(data);
    }

    @Test
    public void test_writeInterfaceResult() throws Exception {
        InterfaceData data = excelUtils.getInterfaceData(testSheet);
        List<InterfaceParameters> interfaceParametersList = data.getParametersList();

        for (int i = 0; i < interfaceParametersList.size(); i++) {
            interfaceParametersList.get(i).getExcelWriteDatas().getWriteDatasMap().get(InterfaceParameters.ACTUAL).setData("result-" + i);

            interfaceParametersList.get(i).getExcelWriteDatas().getWriteDatasMap().get(InterfaceParameters.IS_PASS).setData("isPass-" + i);
        }
        //data.setParametersList(interfaceParametersList);
        excelUtils.writeInterfaceExpectResult(data);
    }

    @Test
    public void test_writeSuiteResult() throws Exception {
        ConfigData configData = excelUtils.getInterfaceConfig();
        List<SuiteConfig> suiteConfigList = configData.getSuiteConfigList();
        for (int i = 0; i < suiteConfigList.size(); i++) {
            suiteConfigList.get(i).getExcelCellData().setData("result集合-" + i);
        }
        configData.setSuiteConfigList(suiteConfigList);
        excelUtils.writeSuiteResult(configData);
    }

    @Test
    public void test_setPublicParameters() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("accesstoken", "ewuiyrwuhudsfjk232442");
        map.put("openid","139278492834");
        map.put("clientid", "2");
        map.put("mobile", "1298492235");
        excelUtils.setPublicParameters(map);
    }

}
