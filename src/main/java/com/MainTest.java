package com;

import com.engine.Excel_Engine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 整个 jar 包（java 工程）的主类
 *
 */

public class MainTest {

    private static Logger logger = LogManager.getLogger();
    /**
     * 在运行 jar 包时，需提供 args 参数
     *
     * @param args 运行时，需提供的参数
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("请在 jar 文件后面添加接口测试用例文档的路径，如：java -jar /your/jar/path/apitest.jar /your/excel/api/test/file/path/api-test.xlsx");
        }
        for (int i = 0; i < args.length; i++) {
            Excel_Engine engine = new Excel_Engine(args[i]);
            engine.runTest();
        }
    }
}
