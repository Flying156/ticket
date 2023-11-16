package org.learn.index12306.biz.payservice.service;

import org.learn.index12306.biz.payservice.dto.RefundReqDTO;
import org.learn.index12306.biz.payservice.dto.RefundRespDTO;

/**
 * 退款接口层
 *
 * @author Milk
 * @version 2023/11/6 21:52
 */
public interface RefundService {

    /**
     * 退款
     *
     * @param requestParam 退款请求参数
     * @return 退款返回
     */
    RefundRespDTO refund(RefundReqDTO requestParam);
}
