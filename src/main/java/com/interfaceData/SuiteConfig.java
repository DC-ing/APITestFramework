package com.interfaceData;

/**
 * 测试配置
 *
 * @version 1.0
 */

public class SuiteConfig {

    public static final String ACTUAL = "测试结果";

    //测试 id
    private String id;
    //接口描述
    private String interfaceName;
    //接口运行模式：是、否
    private String runMode;
    //测试结果
    private ExcelCellData actualesult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRunMode() {
        return runMode;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public ExcelCellData getExcelCellData() {
        return actualesult;
    }

    public void setExcelCellData(ExcelCellData actualesult) {
        this.actualesult = actualesult;
    }

    @Override
    public String toString() {
        return "SuiteConfig{" +
                "id='" + id + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", runMode='" + runMode + '\'' +
                ", actualResult=" + actualesult +
                "}\n";
    }
}