package com.seekisle.adminservice.user.service.impl;

import com.seekisle.adminapi.appuser.domain.dto.UserEditReqDTO;
import com.seekisle.adminapi.appuser.domain.dto.AppUserDTO;
import com.seekisle.adminapi.appuser.domain.dto.AppUserListReqDTO;
import com.seekisle.adminservice.user.config.RabbitConfig;
import com.seekisle.adminservice.user.domain.entity.AppUser;
import com.seekisle.adminservice.user.mapper.AppUserMapper;
import com.seekisle.adminservice.user.service.IAppUserService;
import com.seekisle.commoncore.domain.dto.BasePageDTO;
import com.seekisle.commoncore.utils.AESUtil;
import com.seekisle.commondomain.exception.ServiceException;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * C端用户相关服务实现
 */
@Component
@Slf4j
@RefreshScope
public class AppUserServiceImpl implements IAppUserService {

    /**
     * mapper对象
     */
    @Autowired
    private AppUserMapper appUserMapper;

    /**
     * 用户默认初始图像
     */
    @Value("${appuser.info.defaultAvatar:https://house-file.oss-cn-beijing.aliyuncs.com/house/web/profile/default-avatar.png}")
    private String defaultAvatar;

    /**
     * 消息工具类对象
     */
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 搜索获取人员列表
     *
     * @param appUserListReqDTO 人员列表请求DTO
     * @return C端用户分页结果
     */
    @Override
    public BasePageDTO<AppUserDTO> getUserList(AppUserListReqDTO appUserListReqDTO) {

        AppUserListReqDTO reqDTO = new AppUserListReqDTO();
        BeanUtils.copyProperties(appUserListReqDTO, reqDTO);
        reqDTO.setPhoneNumber(AESUtil.encryptHex(appUserListReqDTO.getPhoneNumber()));

        BasePageDTO<AppUserDTO> result = new BasePageDTO<>();
        Long totals = appUserMapper.selectCount(reqDTO);
        if (0 == totals) {
            // 无数据
            result.setTotals(0);
            result.setTotalPages(0);
            log.info("查询人员列表为空，totals:{}, totalPages:{}, pageNo:{}, pageSize:{}",
                    result.getTotals(), result.getTotalPages(), reqDTO.getPageNo(), reqDTO.getPageSize());
            result.setList(Arrays.asList());
            return result;
        }
        List<AppUser> appUserList = appUserMapper.selectPage(reqDTO);
        result.setTotals(Integer.parseInt(
                String.valueOf(totals)));
        result.setTotalPages(
                BasePageDTO.calculateTotalPages(totals, reqDTO.getPageSize()));
        if (CollectionUtils.isEmpty(appUserList)) {
            // 超过翻页
            log.info("超出查询人员列表范围，totals:{}, totalPages:{}, pageNo:{}, pageSize:{}",
                    result.getTotals(), result.getTotalPages(), reqDTO.getPageNo(), reqDTO.getPageSize());
            result.setList(Arrays.asList());
            return result;
        }
        result.setList(
                appUserList.stream()
                        .map(appUser -> {
                            AppUserDTO appUserDTO = new AppUserDTO();
                            appUserDTO.setUserId(appUser.getId());
                            appUserDTO.setNickName(appUser.getNickName());
                            appUserDTO.setPhoneNumber(
                                    AESUtil.decryptStr(appUser.getPhoneNumber()));
                            appUserDTO.setAvatar(appUser.getAvatar());
                            appUserDTO.setOpenId(appUser.getOpenId());
                            return appUserDTO;
                        }).collect(Collectors.toList())
        );
        return result;

    }

    /**
     * 根据ids获取人员列表
     *
     * @param userIds 用户ID列表
     * @return C端用户DTO列表
     */
    @Override
    public List<AppUserDTO> getUserList(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Arrays.asList();
        }
        List<AppUser> appUserList = appUserMapper.selectBatchIds(userIds);
        return appUserList.stream()
                .map(appUser -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    appUserDTO.setUserId(appUser.getId());
                    appUserDTO.setNickName(appUser.getNickName());
                    appUserDTO.setPhoneNumber(
                            AESUtil.decryptStr(appUser.getPhoneNumber()));
                    appUserDTO.setAvatar(appUser.getAvatar());
                    return appUserDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 编辑用户
     *
     * @param userEditReqDTO 用户编辑DTO
     */
    @Override
    public void edit(UserEditReqDTO userEditReqDTO) {
        AppUser updateAppUser = new AppUser();
        updateAppUser.setId(userEditReqDTO.getUserId());
        updateAppUser.setAvatar(userEditReqDTO.getAvatar());
        updateAppUser.setNickName(userEditReqDTO.getNickName());
        appUserMapper.updateById(updateAppUser);
        // 发消息通知消费方
        AppUser appUser = appUserMapper.selectById(userEditReqDTO.getUserId());
        AppUserDTO appUserDTO = new AppUserDTO();
        BeanUtils.copyProperties(appUser, appUserDTO);
        appUserDTO.setUserId(appUser.getId());
        try{
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, "", appUserDTO);
        } catch (Exception e){
            log.error("生产者发送修改用户消息异常", e);
        }
    }

    /**
     * 手机号注册
     *
     * @param phoneNumber 用户手机号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO registerByPhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new ServiceException("要注册的手机号为空！");
        }
        AppUser appUser = new AppUser();
        appUser.setPhoneNumber(
                AESUtil.encryptHex(phoneNumber));
        // 生成1000到9999之间的随机数
        appUser.setNickName("比特租房用户" + (int)((Math.random() * 9000) + 1000));
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(phoneNumber);
        appUserDTO.setAvatar(appUser.getAvatar());
        return appUserDTO;
    }

    /**
     *微信注册
     *
     * @param openId 用户微信ID
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO registerByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException("要注册的openId为空！");
        }
        AppUser appUser = new AppUser();
        appUser.setOpenId(openId);
        // 生成1000到9999之间的随机数
        appUser.setNickName("比特租房用户" + (int)((Math.random() * 9000) + 1000));
        appUser.setAvatar(defaultAvatar);
        appUserMapper.insert(appUser);
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setOpenId(openId);
        appUserDTO.setAvatar(appUser.getAvatar());
        return appUserDTO;
    }

    /**
     * 根据用户id查询
     * @param userId 用户ID
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findById(Long userId) {
        if (null == userId) {
            return null;
        }
        AppUser appUser = appUserMapper.selectById(userId);
        if (null == appUser) {
            log.error("查询人员信息失败！userId:{}", userId);
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(
                AESUtil.decryptStr(appUser.getPhoneNumber()));
        appUserDTO.setAvatar(appUser.getAvatar());
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }

    /**
     * 根据用户手机号查询
     * @param phoneNumber 用户手机号
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByPhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return null;
        }

        AppUser appUser = appUserMapper.selectByPhoneNumber(
                AESUtil.encryptHex(phoneNumber));
        if (null == appUser) {
            log.error("查询人员信息失败！phoneNumber:{}", phoneNumber);
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(
                AESUtil.decryptStr(appUser.getPhoneNumber()));
        appUserDTO.setAvatar(appUser.getAvatar());
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }

    /**
     * 根据用户微信id查询
     * @param openId 用户微信ID
     * @return C端用户DTO
     */
    @Override
    public AppUserDTO findByOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        AppUser appUser = appUserMapper.selectByOpenId(openId);
        if (null == appUser) {
            log.error("查询人员信息失败！openId:{}", openId);
            return null;
        }
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUserId(appUser.getId());
        appUserDTO.setNickName(appUser.getNickName());
        appUserDTO.setPhoneNumber(
                AESUtil.decryptStr(appUser.getPhoneNumber()));
        appUserDTO.setAvatar(appUser.getAvatar());
        appUserDTO.setOpenId(appUser.getOpenId());
        return appUserDTO;
    }

}
