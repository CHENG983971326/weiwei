package com.neuedu.mapper;

import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int insert(UserInfo record);

    int insertUser(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    List<UserInfo> selectAll();

    List<UserInfo> selectAllByIdAndUserName(@Param("userId")Integer userId,
                                            @Param("userName")String userName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserInfo record);
    /**
     * 校验用户名是否存在
     */
    int checkUsername(String username);

    /**
     * 根据用户名密码查询用户信息
     */
    UserInfo selectUserInfoByUsernameAndPassword(@Param("username")String username,
                                                 @Param("password")String password);

    UserInfo selectUserInfoByUsername(String username);
    /**
     * 校验邮箱是否存在
     */
    int checkEmail(String email);

    /**
     * 更新user信息
     */
    int updateUserBySelectActive(UserInfo userInfo);
}