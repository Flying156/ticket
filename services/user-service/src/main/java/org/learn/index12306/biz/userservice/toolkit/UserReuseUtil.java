package org.learn.index12306.biz.userservice.toolkit;

import static org.learn.index12306.biz.userservice.common.constant.Index12306Constant.USER_REGISTER_REUSE_SHARDING_COUNT;

/**
 * 用户名复用工具类
 *
 * @author Milk
 * @version 2023/10/1 21:03
 */
public final class UserReuseUtil {

    /**
     * 计算分片位置，将已经注销的用户名存入特定的 set 中，在用户注册时，由于用户名唯一，
     * 为了防止在高并发的情况下造成的缓存穿透问题，我们需要把用户名存入布隆过滤器中，但是
     * 布隆过滤器不能删除元素，我们需要使用 set 来辅助判断哪些用户名已经被删除
     */
    public static int hashShardingIdx(String username){
        return Math.abs(username.hashCode() % USER_REGISTER_REUSE_SHARDING_COUNT);
    }
}
