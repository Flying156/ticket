package org.learn.index12306.biz.ticketservice.remote;

import org.learn.index12306.biz.ticketservice.remote.dto.PassengerActualRespDTO;
import org.learn.index12306.biz.ticketservice.remote.dto.PassengerRespDTO;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户远程服务调用
 *
 * @author Milk
 * @version 2023/10/13 20:27
 */
@FeignClient(value = "index12306-user${unique-name:}-service")
public interface UserRemoteService {

    /**
     * 返回乘车人列表
     *
     * @param username 用户姓名
     * @param ids      乘车人 ID 列表
     * @return 乘车了列表
     */
    @GetMapping("/api/user-service/inner/passenger/actual/query/ids")
    Result<List<PassengerRespDTO>> listPassengerQueryByIds(@RequestParam("username") String username, @RequestParam("ids") List<String> ids);
}
