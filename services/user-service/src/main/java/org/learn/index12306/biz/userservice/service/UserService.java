package org.learn.index12306.biz.userservice.service;

import jakarta.validation.constraints.NotEmpty;
import org.learn.index12306.biz.userservice.dto.req.UserUpdateReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryActualRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryRespDTO;

/**
 * 用户信息接口层
 *
 * @author Milk
 * @version 2023/10/1 21:18
 */
public interface UserService {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    UserQueryRespDTO queryUserByUsername(@NotEmpty String username);

    /**
     * 根据用户名查询用户信息,无托名信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    UserQueryActualRespDTO queryActualUserByUsername(String username);


    /**
     * 查询对应的证件的注销次数，过多需要拉黑
     *
     * @param idType  证件类型
     * @param idCard  证件号码
     * @return  相对应的注销次数
     */
    Integer queryUserDeletionNum(Integer idType, String idCard);


    /**
     * 用户信息更新
     *
     * @param requestParam 用户信息入参
     */
    void update(UserUpdateReqDTO requestParam);


}
