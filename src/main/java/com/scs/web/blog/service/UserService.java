package com.scs.web.blog.service;

import com.scs.web.blog.domain.dto.UserDto;
import com.scs.web.blog.domain.dto.UserUpdateDto;
import com.scs.web.blog.util.Result;

import java.sql.SQLException;

/**
 * @author xxcai
 * @ClassName UserService
 * @Description 用户业务逻辑接口
 * @Date 12:01 2019/11/9
 * @Version 1.0
 **/
public interface UserService {
    /**
     * 用户登录功能
     *
     * @param userDto
     * @return
     */
    Result signIn(UserDto userDto);

    /**
     * 获取热门用户信息
     * @return
     */
    Result getHotUsers();

    /**
     * 获取分页用户信息
     * @param currentPage
     * @param count
     * @return
     */
    Result selectByPage(int currentPage, int count);

    /**
     * 根据id查询用户详情数据
     * @param id
     * @return
     */
    Result getUser(long id);

    /**
     * 根据昵称或简介模糊搜索用户
     *
     * @param keywords
     * @return
     */
    Result selectByKeywords(String keywords);

    /**
     * 验证手机号是否可用
     * @param mobile
     * @return Result
     */
    Result checkMobile(String mobile);

    /**
     * 用户注册
     * @param userDto
     * @return Result
     */
    Result signUp(UserDto userDto);

    /**
     * 用户更新
     * @param userUpdateDto
     * @return
     */
    Result updateInfo(UserUpdateDto userUpdateDto);

    /**
     * 获取用户信息
     * @param valueOf
     * @return
     */
    Result getUserInfo(Long valueOf);

    /**
     *
     * @param f_user
     * @param t_user
     * @return
     */
    Result addUserFans(long f_user , long t_user) throws SQLException;

    /**
     *
     * @param f_user
     * @param t_user
     * @return
     */
    Result deleteUserFans(long f_user , long t_user) throws SQLException;

    /**
     *z
     * @param f_user
     * @param t_user
     * @return
     */
    int selectUserFnas(long f_user , long t_user) throws SQLException;


    /**
     *
     * @param userId
     * @param avator
     * @return
     */
    Result updateAvatar(int userId, String avator);


    /**
     *
     * @param userid
     * @return
     */
    Result getArtcileList(long userid) ;
}

