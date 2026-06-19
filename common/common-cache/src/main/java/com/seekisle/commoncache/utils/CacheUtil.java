package com.seekisle.commoncache.utils;

import com.seekisle.commonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 二级缓存工具类
 */
@Slf4j
public class CacheUtil {

    /**
     * 读取二级缓存
     *
     * @param redisService redis服务
     * @param key 缓存key
     * @param typeReference 模板类型
     * @param caffeineCache 本地缓存服务
     * @return 缓存信息
     * @param <T> 缓存类型
     */
    public static  <T> T getL2Cache(RedisService redisService, String key, TypeReference<T> typeReference, Cache<String,Object> caffeineCache){
        T res = (T) caffeineCache.getIfPresent(key);
        if (res!= null){
            log.info("读取本地缓存信息"+ key);
            return res;
        }

        res = redisService.getCacheObject(key,typeReference);

        if (res!= null){
            log.info("读取Redis缓存"+ key);
            // 设置本地缓存
            caffeineCache.put(key,res );
            return res;
        }

        return null;
    }

    /**
     * 设置二级缓存
     *
     * @param redisService redis服务
     * @param key 缓存key
     * @param value  缓存对象值
     * @param caffeineCache 本地缓存信息
     * @param timeout 超时时间
     * @param timeUnit 超时单位
     * @param <T> 对象类型
     */
    public static  <T> void setL2Cache(RedisService redisService, String key, T value,Cache<String,Object> caffeineCache, final Long timeout, final TimeUnit timeUnit){
        // redis缓存
        redisService.setCacheObject(key,value, timeout, timeUnit);
        log.info("更新redis缓存信息"+ key);
        // 本地缓存
        caffeineCache.put(key,value );
        log.info("更新本缓存信息"+ key);
    }
}
