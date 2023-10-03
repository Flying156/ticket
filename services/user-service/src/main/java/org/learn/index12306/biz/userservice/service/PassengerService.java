package org.learn.index12306.biz.userservice.service;

import org.learn.index12306.biz.userservice.dto.req.PassengerRemoveReqDTO;
import org.learn.index12306.biz.userservice.dto.req.PassengerReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.PassengerActualRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.PassengerRespDTO;

import java.util.List;

/**
 * 乘车人接口层
 *
 * @author Milk
 * @version 2023/10/3 11:23
 */
public interface PassengerService {

    /**
     * 通过用户名查询乘车人列表
     *
     * @param username 用户名
     * @return 乘车人列表
     */
    List<PassengerRespDTO> listPassengerQueryByUsername(String username);

    /**
     * 根据乘车人 ID 集合查询乘车人列表
     *
     * @param username 用户名
     * @param ids      乘车人 ID 集合
     * @return 乘车人返回列表
     */
    List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids);


    /**
     * 添加乘车人
     *
     * @param requestParam 添加乘车人请求参数
     */
    void savePassenger(PassengerReqDTO requestParam);

    /**
     * 修改乘车人
     *
     * @param requestParam 修改乘车人请求参数
     */
    void updatePassenger(PassengerReqDTO requestParam);


    /**
     * 移除乘车人
     *
     * @param requestParam 删除乘车人请求参数
     */
    void removePassenger(PassengerRemoveReqDTO requestParam);
}
