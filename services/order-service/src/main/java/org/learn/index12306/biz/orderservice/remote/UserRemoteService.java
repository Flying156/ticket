package org.learn.index12306.biz.orderservice.remote;

import jakarta.validation.constraints.NotEmpty;
import org.learn.index12306.biz.orderservice.remote.dto.UserQueryActualRespDTO;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户远程调用服务
 *
 * @author Milk
 * @version 2023/10/22 20:35
 */
@FeignClient(value = "index12306-user${unique-name:}-service")
public interface UserRemoteService {

    /**
     * 根据乘车人 name 集合查询用户的信息
     */
    @GetMapping("/api/user-service/actual/query")
    Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username") @NotEmpty String username);
}
