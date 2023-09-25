package org.learn.index12306.frameworks.starter.user.core;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

/**
 * 用户上下文
 *
 * @author Milk
 * @version 2023/9/25 16:02
 */
public final class UserContext {

    private static final ThreadLocal<UserInfoDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 添加用户到上下文中
     */
    public static void setUser(UserInfoDTO user){
        USER_THREAD_LOCAL.set(user);
    }

    /**
     * 获取在上下文所有用户的 ID
     */
    public static String getUserId(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUserId).orElse(null);
    }

    /**
     * 获取在上下文所有用户的昵称
     */
    public static String getUsername(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getUsername).orElse(null);
    }

    /**
     * 获取在上下文所有用户的真实姓名
     */
    public static String getRealName(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getRealName).orElse(null);
    }

    /**
     * 获取在上下文所有用户的 token
     */
    public static String getToken(){
        UserInfoDTO userInfoDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userInfoDTO).map(UserInfoDTO::getToken).orElse(null);
    }

    /**
     * 清理用户上下文
     */
    public static void removeUser(){
        USER_THREAD_LOCAL.remove();
    }


}
