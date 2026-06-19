package com.seekisle.adminservice.user.domain.dto;

import com.seekisle.adminservice.user.domain.vo.SysUserVO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * B端用户信息
 */
@Data
public class SysUserDTO implements Serializable {

    /**
     * B端人员id
     */
    private Long userId;

    /**
     * 身份
     */
    private String identity;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 状态
     */
    @NotBlank(message = "状态不能为空")
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * DTO转换VO
     * @return B端用户VO信息
     */
    public SysUserVO convertToVO() {
        SysUserVO sysUserVO = new SysUserVO();
        sysUserVO.setUserId(this.userId);
        sysUserVO.setIdentity(this.identity);
        sysUserVO.setPhoneNumber(this.phoneNumber);
        sysUserVO.setNickName(this.nickName);
        sysUserVO.setStatus(this.status);
        sysUserVO.setRemark(this.remark);
        return sysUserVO;
    }

    /**
     * 校验密码是否合规
     * @return 布尔类型返回
     */
    public Boolean checkPassword() {
        if (StringUtils.isEmpty(this.password)) {
            // 为空，不校验
            return true;
        }
        if (this.password.length() > 20) {
            return false;
        }
        return this.password.matches("^[a-zA-Z0-9]+$");
    }
}
