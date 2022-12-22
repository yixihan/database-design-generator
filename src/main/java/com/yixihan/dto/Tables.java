package com.yixihan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 表结构
 *
 * @author yixihan
 * @since 2022-12-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Tables implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号
     */
    private Long ordinalPosition;
    
    /**
     * 表所在数据库名
     */
    private String tableSchema;

    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 表注释
     */
    private String tableComment;
    
    /**
     * 表备注
     */
    private String tableRemarks;
    
    /**
     * 表所对应的列
     */
    private List<Columns> columnList;
    
    @Override
    public String toString() {
        return "\nTables{" + "tableSchema='" + tableSchema + '\'' + ", tableName='" + tableName + '\'' + ", tableComment='" + tableComment + '\'' + ", columnList=" + columnList + '}';
    }
}
