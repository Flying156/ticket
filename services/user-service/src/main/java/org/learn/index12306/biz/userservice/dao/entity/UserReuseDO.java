package org.learn.index12306.biz.userservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.framework.starter.database.base.BaseDO;

/**
 * 用户名复用表实体
 *
 * @author Milk
 * @version 2023/10/1 15:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_reuse")
public class UserReuseDO extends BaseDO {

    /**
     * 用户名
     */
    private String username;
}
