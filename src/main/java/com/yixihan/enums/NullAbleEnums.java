package com.yixihan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 非空枚举类
 *
 * @author yixihan
 * @date 2022/12/22 13:50
 */
@AllArgsConstructor
@Getter
public enum NullAbleEnums {
    
    /**
     * 可为 null
     */
    YES ("YES", "可为 null"),
    
    /**
     * 不可为 null
     */
    NO ("NO", "不可为 null");
    
    /**
     * 值
     */
    private final String value;
    
    /**
     * 描述
     */
    private final String description;
    
    
}
