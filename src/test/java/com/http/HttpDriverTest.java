package com.http;

import com.interfaceData.ConfigData;
import com.interfaceData.InterfaceData;
import com.interfaceData.InterfaceParameters;
import com.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class HttpDriverTest {

    private ExcelUtils excelUtils;
    private String testSheet;

    private ConfigData configData;
    private InterfaceData interfaceData;
    private HttpDriver driver;

    private MyURI myURI;

    @BeforeClass
    public void setUp() throws Exception {
        String excelPath = System.getProperty("user.dir") + File.separator + "test-datas" + File.separator + "interface-test-demo.xlsx";
        testSheet = "家长版登录";
        excelUtils = new ExcelUtils(excelPath);

        configData = excelUtils.getInterfaceConfig();
        interfaceData = excelUtils.getInterfaceData(testSheet);

        driver = new HttpDriver(configData.getInterfaceMainURL());
    }

    @Test
    public void test_getURI() throws Exception {
        myURI = driver.getFullURI(interfaceData.getInterfaceInfo(), interfaceData.getParametersList().get(0));
        Assert.assertNotNull(myURI);
    }

    @Test
    public void test_Response() throws Exception {
        myURI = driver.getFullURI(interfaceData.getInterfaceInfo(), interfaceData.getParametersList().get(0));
        String responseData = driver.sendRequestAndGetResponse(myURI);
    }

    @Test
    public void test_failRequest() throws Exception {
        myURI = driver.getURI("GET", "htt");
        //返回值为空
        Assert.assertNull(driver.sendRequestAndGetResponse(myURI));
    }
}

