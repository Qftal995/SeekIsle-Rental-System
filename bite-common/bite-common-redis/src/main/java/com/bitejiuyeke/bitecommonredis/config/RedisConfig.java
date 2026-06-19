package com.bitejiuyeke.bitecommonredis.config;

import com.bitejiuyeke.bitecommondomain.constants.CommonConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;

/**
 * redis配置
 */
@EnableCaching
@AutoConfigureBefore(RedisAutoConfiguration.class)
@Configuration
public class RedisConfig {

    /**
     * redis序列化
     * @param redisConnectionFactory redis连接工厂
     * @return redis操作模板
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = createRedisSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 创建序列化器
     * @return redis序列化器
     */
    public GenericJackson2JsonRedisSerializer createRedisSerializer() {

        ObjectMapper objectMapper = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                //不适用默认的dateTime进行序列化,使用JSR310的LocalDateTimeSerializer
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
                //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
                .defaultDateFormat(new SimpleDateFormat(CommonConstants.STANDARD_FORMAT))
                // 只针对非空的值进行序列化
                .configure(MapperFeature.USE_ANNOTATIONS, false)
                //重点,这是序列化LocalDateTIme和LocalDate的必要配置,由Jackson-data-JSR310实现
                .addModule(new JavaTimeModule())
                // 只针对非空的值进行序列化
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();

        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}