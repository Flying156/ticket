package org.learn.index12306.biz.ticketservice.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 席别类型实体
 *
 * @author Milk
 * @version 2023/10/8 15:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatClassDTO {

    /**
     * 席别类型
     */
    private Integer type;

    /**
     * 席别数量
     */
    private Integer quantity;

    /**
     * 席别价格
     */
    private BigDecimal price;

    /**
     * 席别候补标识
     */
    private Boolean candidate;
}
