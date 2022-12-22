package com.yixihan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 列键枚举类
 *
 * @author yixihan
 * @date 2022/12/22 13:44
 */
@AllArgsConstructor
@Getter
public enum ColumnKeyEnums {
    
    /**
     * 主键
     */
    PRI ("PRI", "主键"),
    
    /**
     * 唯一索引
     */
    UNI ("UNI", "唯一索引"),
    
    /**
     * 非唯一索引
     */
    MUL ("MUL", "非唯一索引");

    /**
     * 值
     */
    private final String value;
    
    /**
     * 描述
     */
    private final String description;
    
}
