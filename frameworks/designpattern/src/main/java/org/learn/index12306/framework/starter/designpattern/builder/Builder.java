package org.learn.index12306.framework.starter.designpattern.builder;

import java.io.Serializable;

/**
 * Builder 模式抽象接口
 *
 * @author Milk
 * @version 2023/9/29 11:30
 */
public interface Builder<T> extends Serializable {

    /**
     * 构建方法
     */
    T build();
}
