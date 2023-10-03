package org.learn.index12306.biz.userservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.framework.starter.database.base.BaseDO;

/**
 * 用户注销实体
 *
 * @author Milk
 * @version 2023/10/1 15:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_deletion")
public class UserDeletionDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idCard;

}
