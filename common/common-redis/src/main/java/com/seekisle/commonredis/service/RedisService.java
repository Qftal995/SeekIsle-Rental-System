package com.seekisle.commonredis.service;

import com.seekisle.commoncore.utils.JsonUtil;
import com.seekisle.commondomain.exception.ServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 *
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisService {

    /**
     * redis操作模板类
     */
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param <T> 对象类型
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     * @param <T> 对象类型
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     * @param <T> 对象类型
     * @return 如果key已经存在，则返回false，否则返回true
     */
    public <T> Boolean setCacheObjectIfAbsent(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象
     * @param key 缓存键值
     * @param clazz 对应数据的类
     * @return 缓存键值对应的数据
     * @param <T> 对应数据的类型
     */
    public <T> T getCacheObject(final String key, Class<T> clazz) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);

        if (t == null){
            return null;
        }

        return JsonUtil.string2Obj(JsonUtil.obj2String(t), clazz);
    }

    /**
     * 获得缓存的对象,支持复杂的泛型
     * @param key 缓存键值
     * @param reference 类型模板
     * @return 缓存键值对应的数据
     * @param <T> 对象类型
     */
    public <T> T getCacheObject(final String key, TypeReference<T> reference) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        T t = operation.get(key);

        if (t == null){
            return null;
        }

        return JsonUtil.string2Obj(JsonUtil.obj2String(t), reference);
    }

    /**
     * 删除单个对象
     *
     * @param key key
     * @return 是否成功
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 是否删除了对象
     */
    public boolean deleteObject(final Collection collection) {
        return redisTemplate.delete(collection) > 0;
    }


    /**
     * 计数加一
     *
     * @param key 缓存键值
     * @return  指定数据加一之后的数值
     */
    public Long increment(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 计数减一
     *
     * @param key 缓存键值
     * @return 指定数据减一之后的数值
     */
    public Long decrement(final String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * compare and delete
     *
     * @param key   缓存key
     * @param value value
     * @return 是否完成了比较并删除
     */
    public boolean cad(String key, String value) {
        if (key.contains(StringUtils.SPACE) || value.contains(StringUtils.SPACE)) {
            return false;
        }

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        // 通过lua脚本原子验证令牌和删除令牌
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value);
        return !Objects.equals(result, 0L);
    }

    /**
     * 缓存List数据
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     * @param <T> 对象类型
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 移除List第一个匹配的元素
     *
     * @param key key
     * @param value 值
     * @param <T> 值类型
     */
    public <T> void removeForList(final String key, T value) {
        redisTemplate.opsForList().remove(key, 1L, value);
    }

    /**
     * 移除key下的所有列表元素
     *
     * @param key key
     */
    public void removeForAllList(final String key) {
        redisTemplate.opsForList().remove(key, -1, 0);
    }

    /**
     * 获得缓存的list对象,list中不能有模板
     * @param key key 缓存的键值
     * @param clazz 对象的类
     * @return 列表
     * @param <T> 对象类型
     */
    public <T> List<T> getCacheList(final String key, Class<T> clazz) {
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return JsonUtil.string2List(JsonUtil.obj2String(list), clazz);
    }

    /**
     * 获得缓存的list对象,list中可以支持复杂模板类
     * @param key key信息
     * @param typeReference 类型模板
     * @return list对象
     * @param <T> 对象类型
     */
    public <T> List<T> getCacheList(final String key, TypeReference<List<T>> typeReference) {
        List list = redisTemplate.opsForList().range(key, 0, -1);
        List<T> res = JsonUtil.string2Obj(JsonUtil.obj2String(list), typeReference);
        return res;
    }

    /**
     * 根据范围获取List
     *
     * @param key key
     * @param start 开始位置
     * @param end 结束位置
     * @param clazz 类信息
     * @return List列表
     * @param <T> 类型
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, Class<T> clazz) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2List(JsonUtil.obj2String(range), clazz);
    }

    /**
     * 根据范围获取List
     *
     * @param key key
     * @param start 开始
     * @param end 结果
     * @param typeReference 类型模板
     * @return list列表
     * @param <T> 类型信息
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, TypeReference<List<T>> typeReference) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2Obj(JsonUtil.obj2String(range), typeReference);
    }

    /**
     * 获取指定列表长度
     * @param key key信息
     * @return 列表长度
     */
    public long getCacheListSize(final String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0L : size;
    }

    /**
     * 缓存Set数据
     * @param key key
     * @param dataSet set数据集合
     * @return set操作对象
     * @param <T> 数据类型
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }


    /**
     * 获取set信息
     * @param key key
     * @param typeReference 类型模板
     * @return set信息
     * @param <T> 类型信息
     */
    public <T> Set<T> getCacheSet(final String key, TypeReference<Set<T>> typeReference) {
        Set data = redisTemplate.opsForSet().members(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * set添加元素
     * @param key key
     * @param member 元素信息
     */
    public void addMember(final String key, Object... member) {
        redisTemplate.opsForSet().add(key, member);
    }

    /**
     * 获取有序集合
     *
     * @param key key信息
     * @param typeReference 类型模板
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSet(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set data = redisTemplate.opsForZSet().range(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 根据排序分值删除
     *
     * @param key key
     * @param minScore 最小分
     * @param maxScore 最大分
     */
    public void removeZSetByScore(final String key, double minScore, double maxScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    /**
     * 降序获取有序集合
     * @param key key信息
     * @param typeReference 类型模板
     * @return 降序的有序集合
     * @param <T> 对象类型信息
     */
    public <T> Set<T> getCacheZSetDesc(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set data = redisTemplate.opsForZSet().reverseRange(key, 0, -1);

        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 添加元素
     * @param key key
     * @param value 值
     * @param seqNo 分数
     */
    public void addMemberZSet(String key, Object value, double seqNo) {
        redisTemplate.opsForZSet().add(key, value, seqNo);
    }

    /**
     * 获取hash的map对象
     * @param key key
     * @param dataMap map
     * @param <T> 对象类型
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获取缓存的map支持泛型
     * @param key key
     * @param typeReference 类型模板
     * @return hash对应的map
     * @param <T> 对象类型
     */
    public <T> Map<String, T> getCacheMap(final String key,TypeReference<Map<String, T>> typeReference) {
        Map data= redisTemplate.opsForHash().entries(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 往Hash中存入数据
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     * @param <T> 对象类型
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     * @param <T> 对象类型
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @param typeReference 对象模板
     * @return Hash对象集合
     * @param <T> 对象类型
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys,TypeReference<List<T>> typeReference) {
        List data = redisTemplate.opsForHash().multiGet(key, hKeys);

        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 重命名key
     *
     * @param oldKey 原来key
     * @param newKey 新key
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.keyCommands().rename(oldKey.getBytes(), newKey.getBytes());
            return null;
        });
    }

    /**
     * 入队列
     * @param key key
     * @param value 值
     * @param <T> 值类型
     */
    public <T> void lPush(String key, final T value) {
        redisTemplate.opsForList().leftPush(key, value);
    }
}
