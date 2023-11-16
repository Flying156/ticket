package org.learn.index12306.biz.ticketservice.remote;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Milk
 * @version 2023/11/12 21:26
 */
@FeignClient(value = "index12306-pay${unique-name:}-service")
public interface PayRemoteService {




}
