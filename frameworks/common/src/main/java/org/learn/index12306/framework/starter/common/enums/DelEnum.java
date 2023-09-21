package org.learn.index12306.framework.starter.common.enums;

/**
 * 删除标记枚举
 *
 * @author Milk
 * @version 2023/9/21 10:43
 */
public enum DelEnum {

    /**
     * 正常状态
     */
    NORMAL(0),

    /**
     * 删除状态
     */
    DELETE(1);

    private final Integer statusCode;

    DelEnum(Integer statusCode){
        this.statusCode = statusCode;
    }

    public Integer code(){
        return this.statusCode;
    }

    public String strCode(){
        return String.valueOf(this.statusCode);
    }

    @Override
    public String toString() {
        return strCode();
    }
}
