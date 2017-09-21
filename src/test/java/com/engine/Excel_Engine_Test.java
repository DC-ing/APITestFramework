package com.engine;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class Excel_Engine_Test {

    private Excel_Engine engine;

    @BeforeClass
    public void setUp() throws Exception {
        String excelPath = System.getProperty("user.dir") + File.separator + "test-datas" + File.separator + "interface-engine-test-demo.xlsx";
        engine = new Excel_Engine(excelPath);
    }

    @Test
    public void test_run() throws Exception {
        engine.runTest();
    }

}
