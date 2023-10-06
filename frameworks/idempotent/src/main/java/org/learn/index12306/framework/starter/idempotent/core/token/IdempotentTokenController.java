package org.learn.index12306.framework.starter.idempotent.core.token;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于 Token 验证请求幂等性控制器
 *
 * @author Milk
 * @version 2023/10/6 10:20
 */
@RestController
@RequiredArgsConstructor
public class IdempotentTokenController {

    private final IdempotentTokenService idempotentTokenService;

    @GetMapping("/token")
    public Result<String> createToken() {
        return Results.success(idempotentTokenService.createToken());
    }


}
