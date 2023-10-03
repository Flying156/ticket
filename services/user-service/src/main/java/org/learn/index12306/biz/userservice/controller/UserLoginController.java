package org.learn.index12306.biz.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.userservice.dto.req.UserLoginReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserLoginRespDTO;
import org.learn.index12306.biz.userservice.service.UserLoginService;
import org.learn.index12306.biz.userservice.service.UserService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录控制层
 *
 * @author Milk
 * @version 2023/10/2 20:07
 */
@RestController
@RequiredArgsConstructor
public class UserLoginController {


    private final UserLoginService userLoginService;
    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/api/user-service/v1/login")
    public Result<UserLoginRespDTO> login(@RequestBody @Valid UserLoginReqDTO requestParam){
        return Results.success(userLoginService.login(requestParam));
    }


    /**
     * 通过 Token 检查用户是否登录
     */
    @GetMapping("/api/user-service/check-login")
    public Result<UserLoginRespDTO> checkLogin(@RequestParam("accessToken") @NotEmpty String accessToken){
        UserLoginRespDTO result = userLoginService.checkLogin(accessToken);
        return Results.success(result);
    }

    /**
     * 用户退出登录
     */
     @GetMapping("/api/user-service/logout")
    public Result<Void> logout(@RequestParam("accessToken") @NotEmpty String accessToken){
        userLoginService.logout(accessToken);
        return Results.success();
    }

}
