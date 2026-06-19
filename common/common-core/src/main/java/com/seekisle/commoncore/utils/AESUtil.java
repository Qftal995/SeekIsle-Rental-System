package com.seekisle.commoncore.utils;


import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * AES加密工具类
 */
@Slf4j
public class AESUtil {

    /**
     * keys
     */
    private static final byte[] KEYS = "12345678abcdefgh".getBytes(StandardCharsets.UTF_8);

    /**
     * aes加密
     *
     * @param data 数据
     * @return 加密后数据
     */
    public static String encryptHex(String data) {
        try {
            return StringUtils.isEmpty(data) ? null : SecureUtil.aes(KEYS).encryptHex(data);
        } catch (Exception e) {
            log.error("aes加密失败", e);
            return null;
        }
    }

    /**
     * aes解密
     *
     * @param data 数据
     * @return 解密后数据
     */
    public static String decryptStr(String data) {
        try {
            return StringUtils.isEmpty(data) ? null : SecureUtil.aes(KEYS).decryptStr(data);
        } catch (Exception e) {
            log.error("aes解密失败", e);
            return null;
        }
    }

}
