package org.learn.index12306.biz.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.userservice.dao.entity.UserDeletionDO;
import org.learn.index12306.biz.userservice.dto.req.UserDeletionReqDTO;
import org.learn.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.learn.index12306.biz.userservice.dto.req.UserUpdateReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryActualRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserRegisterRespDTO;
import org.learn.index12306.biz.userservice.service.UserLoginService;
import org.learn.index12306.biz.userservice.service.UserService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 *
 * @author Milk
 * @version 2023/10/1 21:10
 */
@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;
    private final UserLoginService userLoginService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/user-service/query")
    public Result<UserQueryRespDTO> queryUserByUsername(@RequestParam("username") @NotEmpty String username){
        return Results.success(userService.queryUserByUsername(username));
    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
    @GetMapping("/api/user-service/actual/query")
    public Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username") @NotEmpty String username){
        return Results.success(userService.queryActualUserByUsername(username));
    }

    /**
     * 判断用户名是否存在
     */
    @GetMapping("/api/user-service/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") @NotEmpty String username){
        return Results.success(userLoginService.hasUsername(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/user-service/register")
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO requestParam){
        return Results.success(userLoginService.register(requestParam));
    }


    /**
     * 修改用户
     */
    @PostMapping("/api/user-service/update")
    public Result<Void> update(@RequestBody @Valid UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户注销
     */
    @PostMapping("/api/user-service/deletion")
    public Result<Void> deletion(@RequestBody @Valid UserDeletionReqDTO requestParam){
        userLoginService.deletion(requestParam);
        return Results.success();
    }
}
