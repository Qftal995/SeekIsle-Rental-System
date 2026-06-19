package com.seekisle.portalservice.user.entity.dto;

import com.seekisle.commonsecurity.domain.dto.LoginUserDTO;
import com.seekisle.portalservice.user.entity.vo.UserVO;
import lombok.Data;

/**
 * 用户信息
 */
@Data
public class UserDTO extends LoginUserDTO {

    /**
     * 用户头像
     */
    private String avatar;

    public UserVO convertToVO() {
        UserVO userVO = new UserVO();
        userVO.setUserId(this.getUserId());
        userVO.setAvatar(this.avatar);
        userVO.setNickName(this.getUserName());
        userVO.setToken(this.getToken());
        userVO.setUserName(this.getUserName());
        userVO.setLoginTime(this.getLoginTime());
        userVO.setExpireTime(this.getExpireTime());
        return userVO;
    }

}
