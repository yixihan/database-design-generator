package com.yixihan.generator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.yixihan.dto.Columns;
import com.yixihan.dto.Tables;
import com.yixihan.enums.ColumnKeyEnums;
import com.yixihan.enums.NullAbleEnums;
import com.yixihan.util.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库设计生成器
 *
 * @author yixihan
 * @date 2022/12/22 9:26
 */
public class DatabaseDesignGenerator {
    
    /**
     * 配置文件路径
     */
    private static final String CONFIG_PATH = "/application.properties";
    
    /**
     * 启用配置文件路径前缀
     */
    private static final String PREFIX_PATH = "/config/%s";
    
    /**
     * information_schema 表名
     */
    private static final String INFORMATION_SCHEMA_NAME = "information_schema";
    
    private static final String YES = "是";
    
    private static final String NO = "否";
    
    /**
     * 数据库表
     */
    private static final List<Tables> TABLES_LIST = new ArrayList<> ();
    
    private static final Log log = LogFactory.get ();
    
    public static void main(String[] args) throws Exception {
        // 加载主配置文件
        InputStream configParams = DatabaseDesignGenerator.class.getResourceAsStream (CONFIG_PATH);
        Properties configProps = new Properties ();
        configProps.load (configParams);
        
        // 获取启用的配置文件
        String enableConfigPath = String.format (PREFIX_PATH, configProps.getProperty ("enable.config"));
        // 加载启用的配置文件
        InputStream enableParams = DatabaseDesignGenerator.class.getResourceAsStream (enableConfigPath);
        Properties props = new Properties ();
        props.load (enableParams);
        
        // 数据源配置
        Class.forName (props.getProperty ("jdbc.driver"));
        
        // 获取需要生成的表
        log.info ("获取 {} 表列表..", props.getProperty ("jdbc.database"));
        getTables (props);
        log.info ("{} 表列表获取成功, 共 {} 张表", props.getProperty ("jdbc.database"), TABLES_LIST.size ());
        
        // 获取表的详细列结构
        for (Tables table : TABLES_LIST) {
            log.info ("获取 {} 表详细结构...", table.getTableName ());
            getColumns (table, props);
            log.info ("获取 {} 表详细结构成功!", table.getTableName ());
        }
        
        // 写出到 excel 文件
        writerExcel (props);
        log.info ("excel生成完毕!");
        
    }
    
    /**
     * 将结果输出到 excel 中
     *
     * @param props 配置
     */
    private static void writerExcel(Properties props) {
        
        String projectPath = System.getProperty ("user.dir");
        String dir = props.getProperty ("custom.out.dir");
        String fileName = props.getProperty ("custom.out.fileName");
    
        // 清空输出目录
        String pathname = projectPath + "/" + dir;
        log.info ("清空输出目录 : {}...", pathname);
        FileUtils.emptyDir (new File (pathname));
        log.info ("生成excel..");
        
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter (pathname + "/" + fileName);
    
        writeTableExcel (writer);
        
        writer = ExcelUtil.getWriter (pathname + "/" + fileName);
    
        for (Tables table : TABLES_LIST) {
            writeColumnsExcel (writer, table);
        }
        
        writer.flush ();
    
    }
    
    private static void writeTableExcel (ExcelWriter writer) {
        // 设置 sheet 名
        writer.setSheet ("数据库表设计-表清单");
        
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(3, "表清单");
        
        //自定义标题别名
        writer.addHeaderAlias("ordinalPosition", "序号");
        writer.addHeaderAlias("tableComment", "中文名称");
        writer.addHeaderAlias("tableName", "物理表名");
        writer.addHeaderAlias("tableRemarks", "备注");
    
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        
        // 写入数据
        writer.write (TABLES_LIST, Boolean.TRUE);
    
        // 设置列为自动宽度
        writer.autoSizeColumnAll ();
        
        // 写入文件
        writer.flush ();
    }
    
    private static void writeColumnsExcel (ExcelWriter writer, Tables table) {
        // 设置 sheet 名
        writer.setSheet (table.getTableComment ());
    
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(6, table.getTableComment ());
    
        //自定义标题别名
        writer.addHeaderAlias("ordinalPosition", "序号");
        writer.addHeaderAlias("columnComment", "中文名称");
        writer.addHeaderAlias("columnName", "列名");
        writer.addHeaderAlias("dataType", "数据类型");
        writer.addHeaderAlias("primaryKey", "主键");
        writer.addHeaderAlias("nullAbleKey", "非空");
        writer.addHeaderAlias("foreignKey", "外键");
    
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
    
        // 写入数据
        writer.write (table.getColumnList (), Boolean.TRUE);
    
        // 设置列为自动宽度
        writer.autoSizeColumnAll ();
    }
    
    /**
     * 获取需要生成的数据库设计表具体表结构
     *
     * @param table 表名
     * @param props 配置
     */
    private static void getColumns(Tables table, Properties props) throws SQLException {
        Connection connection = getConnection (props);
        String sql = "select `TABLE_SCHEMA`, `TABLE_NAME`, `COLUMN_NAME`, `ORDINAL_POSITION`," +
                "`IS_NULLABLE`, `DATA_TYPE`, `COLUMN_KEY`, `EXTRA`, `COLUMN_COMMENT` " +
                "from `COLUMNS` " +
                "where `TABLE_SCHEMA` = ? and `TABLE_NAME` = ?" ;
        
        // 填充占位符
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString (1, table.getTableSchema ());
        st.setString (2, table.getTableName ());
        // 执行
        ResultSet resultSet = st.executeQuery ();
        
        // 获取结果
        List<Columns> columnList = new ArrayList<> ();
        while (resultSet.next ()) {
            columnList.add (init (resultSet));
        }
    
        table.setColumnList (columnList);
    }
    
    /**
     * 根据结果, 生成 {@link Columns}
     */
    private static Columns init (ResultSet resultSet) throws SQLException {
        Columns column = new Columns ();
        column.setTableSchema (resultSet.getString ("TABLE_SCHEMA"));
        column.setTableName (resultSet.getString ("TABLE_NAME"));
        column.setColumnName (resultSet.getString ("COLUMN_NAME"));
        column.setOrdinalPosition (resultSet.getLong ("ORDINAL_POSITION"));
        column.setIsNullable (resultSet.getString ("IS_NULLABLE"));
        column.setDataType (resultSet.getString ("DATA_TYPE"));
        column.setColumnKey (resultSet.getString ("COLUMN_KEY"));
        column.setExtra (resultSet.getString ("EXTRA"));
        column.setColumnComment (resultSet.getString ("COLUMN_COMMENT"));
        column.setForeignKey (NO);
        setNullAbleKey (column);
        setPrimaryKey (column);
        return column;
    }
    
    /**
     * 确定该列是否为非空列
     *
     * @param column 列
     */
    private static void setNullAbleKey(Columns column) {
        if (NullAbleEnums.YES.getValue ().equals (column.getIsNullable ())) {
            column.setNullAbleKey (NO);
        } else if (NullAbleEnums.NO.getValue ().equals (column.getIsNullable ())) {
            column.setNullAbleKey (YES);
        } else {
            log.error ("异常 : 未指定是否可为空!");
        }
    }
    
    /**
     * 确定该列是否为主键
     * 
     * @param column 列
     */
    private static void setPrimaryKey (Columns column) {
        if (ColumnKeyEnums.PRI.getValue ().equals (column.getColumnKey ())) {
            column.setPrimaryKey (YES);
        } else {
            column.setPrimaryKey (NO);
        }
    }
    
    /**
     * 获取需要生成数据库设计表的表
     *
     * @param props 配置
     */
    private static void getTables(Properties props) throws SQLException {
        // 获取配置文件内表名
        String tables = props.getProperty ("custom.tableNames");
        // 获取配置文件内数据库名
        String database = props.getProperty ("jdbc.database");
        // 表序号
        long order = 1L;
        // 获取数据库连接
        Connection connection = getConnection (props);
        // 组装sql
        String sql;
        if (StrUtil.isNotBlank (tables)) {
            String inSql = Arrays.stream (tables.split (","))
                    .map ((o) -> "'" + o + "'")
                    .collect (Collectors.joining (",", "(", ")"));
            sql = "select `TABLE_SCHEMA`, `TABLE_NAME`, `TABLE_COMMENT`" +
                    "from `TABLES` " +
                    "where `TABLE_SCHEMA` = ? and `TABLE_NAME` in (?)";
            sql = sql.replace ("(?)", inSql);
        } else {
            sql = "select `TABLE_SCHEMA`, `TABLE_NAME`, `TABLE_COMMENT`" +
                    "from `TABLES` " +
                    "where `TABLE_SCHEMA` = ?";
        }
        
        // 填充占位符
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString (1, database);
        // 执行
        ResultSet resultSet = st.executeQuery ();
        // 获取结果
        while (resultSet.next ()) {
            Tables table = new Tables ();
            table.setTableSchema (resultSet.getString ("TABLE_SCHEMA"));
            table.setTableName (resultSet.getString ("TABLE_NAME"));
            table.setTableComment (resultSet.getString ("TABLE_COMMENT"));
            table.setTableRemarks (table.getTableComment ());
            table.setOrdinalPosition (order++);
            TABLES_LIST.add (table);
        }
    }
    
    /**
     * 获取 jdbc 连接
     *
     * @param props 配置
     * @return {@link Connection}
     */
    private static Connection getConnection(Properties props) throws SQLException {
        String url = props.getProperty ("jdbc.url") + "/" + 
                DatabaseDesignGenerator.INFORMATION_SCHEMA_NAME + 
                props.getProperty ("jdbc.suffix");
        String username = props.getProperty ("jdbc.username");
        String password = props.getProperty ("jdbc.password");
        return DriverManager.getConnection (url, username, password);
    }
}