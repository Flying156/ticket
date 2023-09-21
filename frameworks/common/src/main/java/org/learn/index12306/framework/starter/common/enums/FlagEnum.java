package org.learn.index12306.framework.starter.common.enums;

/**
 * @author Milk
 * @version 2023/9/21 17:16
 */
public enum FlagEnum {

    FALSE(0),

    TRUE(1);


    private final Integer flag;

    FlagEnum(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag(){
        return this.flag;
    }

    public String strFlag(){
        return String.valueOf(flag);
    }


    @Override
    public String toString() {
        return this.strFlag();
    }
}
