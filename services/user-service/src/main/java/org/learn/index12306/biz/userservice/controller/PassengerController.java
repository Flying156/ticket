package org.learn.index12306.biz.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.userservice.dto.req.PassengerRemoveReqDTO;
import org.learn.index12306.biz.userservice.dto.req.PassengerReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.PassengerActualRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.PassengerRespDTO;
import org.learn.index12306.biz.userservice.service.PassengerService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.user.core.UserContext;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 乘车人控制层
 *
 * @author Milk
 * @version 2023/10/1 21:09
 */
@RestController
@RequiredArgsConstructor
public class PassengerController {


    private final PassengerService passengerService;

    /**
     * 根据用户名查询乘车人列表
     */
    @GetMapping("/api/user-service/passenger/query")
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername(){
        return Results.success(passengerService.listPassengerQueryByUsername(UserContext.getUsername()));
    }


    /**
     * 根据乘车人 ID 集合查询乘车人列表
     */
    @GetMapping("/api/user-service/inner/passenger/actual/query/ids")
    public Result<List<PassengerActualRespDTO>> listPassengerQueryByIds(@RequestParam("username") String username, @RequestParam("ids") List<Long> ids) {
        return Results.success(passengerService.listPassengerQueryByIds(username, ids));
    }

    /**
     * 添加乘车人
     */
    @PostMapping("/api/user-service/passenger/save")
    public Result<Void> savePassenger(@RequestBody PassengerReqDTO requestParam){
        passengerService.savePassenger(requestParam);
        return Results.success();
    }

    /**
     * 修改乘车人
     */
    @PostMapping("/api/user-service/passenger/update")
    public Result<Void> updatePassenger(@RequestBody PassengerReqDTO requestParam){
        passengerService.updatePassenger(requestParam);
        return Results.success();
    }


    public Result<Void> removePassenger(@RequestBody PassengerRemoveReqDTO requestParam){
        passengerService.removePassenger(requestParam);
        return Results.success();
    }

}
