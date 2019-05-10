package com.neuedu.service;

import com.neuedu.common.ResultModel;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.pojo.UserInfo;


public interface IUserService {
    /**
     * 登录接口
     */
    String login(String username, String password);

    /**
     *注册接口
     */
    ResultModel register(UserInfo userInfo);

/**
 * 根据userid查询用户信息
 */
UserInfo findUserInfoByUserid(Integer userId);
}

