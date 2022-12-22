package com.yixihan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 列结构
 *
 * @author yixihan
 * @since 2022-12-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Columns implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 列所在数据库名
     */
    private String tableSchema;
    
    /**
     * 列所在表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 序号
     */
    private Long ordinalPosition;

    /**
     * 是否为空
     */
    private String isNullable;

    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 索引类型
     */
    private String columnKey;

    /**
     * 额外属性 (自增, ...)
     */
    private String extra;

    /**
     * 注释
     */
    private String columnComment;
    
    /**
     * 主键
     */
    private String primaryKey;
    
    /**
     * 外键
     */
    private String foreignKey;
    
    /**
     * 非空
     */
    private String nullAbleKey;
    
    @Override
    public String toString() {
        return "\nColumns{" + "tableSchema='" + tableSchema + '\'' + ", tableName='" + tableName + '\'' + ", columnName='" + columnName + '\'' + ", ordinalPosition=" + ordinalPosition + ", isNullable='" + isNullable + '\'' + ", dataType='" + dataType + '\'' + ", columnKey='" + columnKey + '\'' + ", extra='" + extra + '\'' + ", columnComment='" + columnComment + '\'' + ", primaryKey=" + primaryKey + ", foreignKey=" + foreignKey + ", nullAbleKey=" + nullAbleKey + '}';
    }
}
