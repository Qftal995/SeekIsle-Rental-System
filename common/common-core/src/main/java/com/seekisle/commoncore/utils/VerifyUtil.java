package com.seekisle.commoncore.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用校验工具类
 */
public class VerifyUtil {

    /**
     * 手机校验正则
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile("^1[2|3|4|5|6|7|8|9][0-9]\\d{8}$");

    /**
     * 校验码指定范围字符串
     */
    private static final String NUMBER_VERIFY_CODES = "1234567890";

    /**
     * 手机号校验
     *
     * @param phone 手机号
     * @return 11位，且以1开头，第二位是2-9
     */
    public static boolean checkPhone(String phone) {
        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }


    /**
     * 生成验证码
     *
     * @param verifySize 验证码长度
     * @return 验证码
     */
    public static String generateVerifyCode(int verifySize) {
        String sources = NUMBER_VERIFY_CODES;
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }
}
