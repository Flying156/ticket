package org.learn.index12306.biz.userservice.common.constant;

/**
 * 系统级公共常量
 *
 * @author Milk
 * @version 2023/10/1 20:03
 */
public final class Index12306Constant {

    /**
     * 用户注册可复用用户名分片数
     * <p>
     * 对于已经删除的用户名，我们把这些用户名存入 set 中，同时为了防止恶意用户反复操作，set 存储过大
     * 需要将用户名进行分片，降低压力
     * </p>
     */
    public static final int USER_REGISTER_REUSE_SHARDING_COUNT = 1024;
}
