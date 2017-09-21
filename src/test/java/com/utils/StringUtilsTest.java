package com.utils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StringUtilsTest {

    private String content;

    @BeforeClass
    public void setUp() throws Exception {
        content = "\"token\":\"sfsfV24Vrty57vAjkDkjdfs\",\"openid\":2346462,\"name\":\"测试名字\"";
    }

    @Test
    public void test_getMatchString() throws Exception {
        String regex = "\"openid\":[0-9]+";
        String matchString = StringUtils.getMatchString(content, regex);
        System.out.println("matchString：" + matchString);
        Assert.assertNotNull(matchString);
    }

    @Test
    public void test_isMatchAll() throws Exception {
        String regex = "\"token\":\"[A-Za-z0-9_]+\",\"openid\":[0-9]+,\"name\":\"[\\S]+\"";
        boolean isMatchAll = StringUtils.isMatchAll(content, regex);
        System.out.println("isMatchAll：" + isMatchAll);
        Assert.assertTrue(isMatchAll);
    }

    @Test
    public void test_isMatchPart() throws Exception {
        String regex = "\"token\":\"[A-Za-z0-9_]+\"";
        boolean isMatchPart = StringUtils.isMatchPart(content, regex);
        System.out.println("isMatchPart：" + isMatchPart);
        Assert.assertTrue(isMatchPart);
    }

    @Test
    public void test_splitString() throws Exception {
        String[] splits = StringUtils.getSplitString(content,",");
        String log = "splitString：";
        for (String split : splits) {
            log = log + split + ",";
        }
        System.out.println(log);
        Assert.assertNotNull(splits);
    }

}
