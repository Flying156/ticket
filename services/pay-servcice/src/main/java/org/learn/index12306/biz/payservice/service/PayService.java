package org.learn.index12306.biz.payservice.service;

import org.learn.index12306.biz.payservice.dto.PayCallbackReqDTO;
import org.learn.index12306.biz.payservice.dto.PayInfoRespDTO;
import org.learn.index12306.biz.payservice.dto.PayRespDTO;
import org.learn.index12306.biz.payservice.dto.base.PayRequest;

/**
 * 支付接口层
 *
 * @author Milk
 * @version 2023/11/6 21:51
 */
public interface PayService {

    /**
     * 支付
     *
     * @param requestParam 支付请求参数
     * @return 支付返回结果
     */
    PayRespDTO commonPay(PayRequest requestParam);

    /**
     * 根据订单号查询支付单详情
     *
     * @param orderSn 订单号
     * @return 支付单详情
     */
    PayInfoRespDTO getPayInfoByOrderSn(String orderSn);

    /**
     * 根据支付号查询支付单详情
     *
     * @param paySn 支付号
     * @return 支付单详情
     */
    PayInfoRespDTO getPayInfoByPaySn(String paySn);

    /**
     * 支付回调
     *
     * @param requestParam 请求参数
     */
    void callbackPay(PayCallbackReqDTO requestParam);
}
