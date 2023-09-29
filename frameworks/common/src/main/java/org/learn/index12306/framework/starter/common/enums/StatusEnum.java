package org.learn.index12306.framework.starter.common.enums;

/**
 * 状态枚举
 *
 * @author Milk
 * @version 2023/9/27 13:14
 */
public enum StatusEnum {

    SUCCESS(1),

    FAILURE(0);

    private final Integer code;


    StatusEnum(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

    public String strCode(){
        return String.valueOf(code);
    }


    @Override
    public String toString() {
        return this.strCode();
    }



}
