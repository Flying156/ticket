package org.learn.index12306.biz.userservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.framework.starter.database.base.BaseDO;

/**
 * 用户手机号实体对象
 *
 * @author Milk
 * @version 2023/10/1 15:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_phone")
public class UserPhoneDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 注销时间戳
     */
    private Long deletionTime;
}
