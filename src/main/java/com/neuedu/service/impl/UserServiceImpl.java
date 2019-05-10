package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ResultModel;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class UserServiceImpl implements IUserService {
    public static final Map<String,Object> map1=new HashMap<>();
     Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public String login(String username, String password) {
        //step1:参数非空校验
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("用户名不为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("密码不为空");
        }
        //step2:检查用户名是否存在
        int result = userInfoMapper.checkUsername(username);
        if (result == 0) {
            return "用户名不存在";
        }

        //step3:根据用户名密码查询用户信息

        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username, MD5Utils.getMD5Code(password));
        if (userInfo == null) {
            return "用户名或者密码错误";
        }
        if (userInfo.getRole()==Const.RoleEnum.ROLE_CUSTOMER.getCode()){
            userInfo.setPassword("");
            return "普通用户登录";
        }
        //返回结果
       else {
            userInfo.setPassword("");
            return "redirect:/user/manager";
        }
    }

    @Override
    public ResultModel register(UserInfo userInfo) {
        //step1:参数非空校验
        //step2：校验用户名
        int result = userInfoMapper.checkUsername(userInfo.getUsername());
        if (result > 0) {
            return ResultModel.build(10,"用户名已存在");
        }
        //step3：校验邮箱
        int result_email = userInfoMapper.checkEmail(userInfo.getEmail());
        if (result_email > 0) {
            return ResultModel.build(10,"邮箱已存在");
        }
        //step4：注册
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_CUSTOMER.getCode()){
            return ResultModel.build(10,"限制仅可普通用户注册");
        }
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int count = userInfoMapper.insertUser(userInfo);
        if (count > 0) {
            return ResultModel.ok();
        }
        //step5：返回结果
        return ResultModel.build(0,"注册失败");
    }



    @Override
    public UserInfo findUserInfoByUserid(Integer userId) {
       return userInfoMapper.selectByPrimaryKey(userId);

    }
}




