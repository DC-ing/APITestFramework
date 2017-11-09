package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 使用正则表达式提取字符串内容
     *
     * @param content 提取的字符串
     * @param regex 正则表达式
     *
     * @return 提取内容
     */
    public static String getMatchString(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    /**
     * 判断当前正则表达式是否匹配整个字符串
     *
     * @param content 匹配的字符串
     * @param regex 正则表达式
     *
     * @return 是否匹配成功
     */
    public static boolean isMatchAll(String content, String regex) {
        String matchResult = StringUtils.getMatchString(content, regex);
        if (matchResult != null && matchResult.equals(content)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前正则表达式是否匹配部分字符串
     *
     * @param content 匹配的字符串
     * @param regex 正则表达式
     *
     * @return 是否匹配成功
     */
    public static boolean isMatchPart(String content, String regex) {
        String matchResult = StringUtils.getMatchString(content, regex);
        if (matchResult != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分隔字符串
     *
     * @param split 分隔的字符串
     * @param regex 分隔符（支持正则表达式）
     *
     * @return 分隔后的字符串数组
     */
    public static String[] getSplitString(String split, String regex) {
        return split.split(regex);
    }

    /**
     * 判断输入的字符串参数是否为空。
     *
     * @param content 输入的字符串
     *
     * @return 输入的字符串为空则返回true,反之则返回false
     */
    public static boolean isEmpty(String content) {
        if (content == null || content.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断输入的字符串参数是否不为空。
     *
     * @param content 输入的字符串
     *
     * @return 输入的字符串不为空则返回true,反之则返回false
     */
    public static boolean isNotEmpty(String content) {
        if(content != null && content.length() != 0) {
            return true;
        } else {
            return false;
        }
    }
}