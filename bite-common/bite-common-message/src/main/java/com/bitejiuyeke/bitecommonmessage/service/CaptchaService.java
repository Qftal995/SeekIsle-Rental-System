package com.bitejiuyeke.bitecommonmessage.service;

import com.bitejiuyeke.bitecommoncore.utils.VerifyUtil;
import com.bitejiuyeke.bitecommondomain.domain.ResultCode;
import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommondomain.constants.MessageConstants;
import com.bitejiuyeke.bitecommonredis.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现
 */
@Service
@RefreshScope
public class CaptchaService {

    /**
     * redis服务
     */
    @Autowired
    private RedisService redisService;

    /**
     * 阿里云短信服务
     */
    @Autowired
    private AliSmsService aliSmsService;

    /**
     * 每日发送次数限制
     */
    @Value("${sms.send-limit:50}")
    private Integer sendLimit;
    /**
     * 是否发送，默认不发送使用123456验证码
     */
    @Value("${sms.send-message:false}")
    private boolean sendMessage;
    /**
     * 过期时间，单位分钟，默认5分钟
     */
    @Value("${sms.code-expiration:5}")
    private Long phoneCodeExpiration;

    /**
     * 发送验证码
     * @param phone 电话号码
     * @return 验证码
     */
    public String sendCode(String phone) {

        // 1、校验是否超过每日限制
        if (!VerifyUtil.checkPhone(phone)) {
            throw new ServiceException(ResultCode.ERROR_PHONE_FORMAT);
        }

        String limitCacheKey = MessageConstants.SMS_CODE_TIMES_KEY + phone;
        Integer times = redisService.getCacheObject(limitCacheKey, Integer.class);
        times = times == null ? 0 : times;
        if (times >= sendLimit) {
            throw new ServiceException(ResultCode.SEND_MSG_OVERLIMIT);
        }

        // 2、校验是否在1分钟之内频繁发送
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        String cacheValue = redisService.getCacheObject(cacheKey, String.class);
        long expireTime = redisService.getExpire(cacheKey);
        if (!StringUtils.isEmpty(cacheValue) && expireTime > phoneCodeExpiration * 60 - 60) {
            long time = expireTime - (phoneCodeExpiration * 60 - 60);
            throw new ServiceException("操作频繁，请在" + time + "秒之后再试", ResultCode.INVALID_PARA.getCode());
        }

        // 3、生成验证码
        String verifyCode = sendMessage ? VerifyUtil.generateVerifyCode(MessageConstants.DEFAULT_SMS_LENGTH) : MessageConstants.DEFAULT_SMS_CODE;

        // 4、将验证码和验证码的发送次数存储到redis
        if (sendMessage) {
            boolean sendMobile = aliSmsService.sendMobileCode(phone, verifyCode);
            if (!sendMobile) {
                throw new ServiceException(ResultCode.SEND_MSG_FAILED);
            }
        }

        redisService.setCacheObject(cacheKey, verifyCode, phoneCodeExpiration, TimeUnit.MINUTES);

        //5. 限制1天内的短信次数
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        redisService.setCacheObject(limitCacheKey, times + 1, seconds, TimeUnit.SECONDS);
        return verifyCode;
    }

    /**
     * 校验验证码是否正确
     *
     * @param phone 电话号码
     * @param code 验证码
     * @return 是否ok
     */
    public boolean checkCode(String phone, String code) {
        // 校验验证码
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        String cacheValue = redisService.getCacheObject(cacheKey, String.class);
        if (StringUtils.isEmpty(cacheValue)) {
            throw new ServiceException(ResultCode.INVALID_CODE);
        }
        if (!cacheValue.equals(code)) {
            throw new ServiceException(ResultCode.ERROR_CODE);
        }
        // 删除缓存
        redisService.deleteObject(cacheKey);
        return true;
    }

    /**
     * 从缓存中获取验证码
     *
     * @param phone 电话号码
     * @return 验证码
     */
    public String getCode(String phone) {
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        return redisService.getCacheObject(cacheKey, String.class);
    }

    /**
     * 从缓存中删除验证码
     *
     * @param phone 电话号码
     * @return 是否删除成功
     */
    public boolean deleteCode(String phone) {
        String cacheKey = MessageConstants.SMS_CODE_KEY + phone;
        return redisService.deleteObject(cacheKey);
    }
}
