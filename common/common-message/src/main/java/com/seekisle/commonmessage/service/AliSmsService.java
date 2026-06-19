package com.seekisle.commonmessage.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.seekisle.commoncore.utils.JsonUtil;

import com.seekisle.commondomain.constants.MessageConstants;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信操作组件
 */
@Component
@Slf4j
@RefreshScope
public class AliSmsService {

    /**
     * 客户端
     */
    @Autowired
    private Client client;

    /**
     * 默认模板代码，一般为注册短信的代码
     */
    @Value("${sms.aliyun.templateCode:}")
    private String templateCode;

    /**
     * 是否发送短信，不发送往往用于测试的时候
     */
    @Value("${sms.send-message:true}")
    private boolean sendMessage;

    /**
     * 签名配置
     */
    @Value("${sms.sing-name:}")
    private String singName;

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否陈宫
     */
    public boolean sendMobileCode(String phone, String code) {
        Map<String, String> params = new HashMap<>(1);
        params.put("code", code);
        return sendTempMessage(phone, templateCode, params);
    }

    /**
     * 发送模板消息
     *
     * @param phone        手机号
     * @param templateCode 模板code
     * @param params       参数
     * @return 是否成功
     */
    public boolean sendTempMessage(String phone, String templateCode,
                                   Map<String, String> params) {
        if (!sendMessage) {
            log.error("短信发送通道关闭，发送失败......{}", phone);
            return false;
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setSignName(singName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(JsonUtil.obj2String(params));
        try {
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            SendSmsResponseBody responseBody = sendSmsResponse.getBody();
            if (!MessageConstants.SMS_MSG_OK.equalsIgnoreCase(responseBody.getCode())) {
                log.error("短信{} 发送失败，失败原因:{}.... ", new Gson().toJson(sendSmsRequest), responseBody.getMessage());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("短信{} 发送失败，失败原因:{}.... ", new Gson().toJson(sendSmsRequest), e.getMessage());
            return false;
        }
    }
}
