# database-design-generator

用于自动生成数据库设计表, 以 excel 文件形式展示



## 使用教程

### 在 resource 目录下新建配置文件

新建 xxx.properties 文件, 内容可参考 [example.properties](src/main/resources/config/example.properties)

![image-20221222160417123](https://typora-oss.yixihan.chat//img/202212221604457.png)



#### 配置介绍

```properties
# 数据库配置
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306
jdbc.database=example
jdbc.suffix=?serverTimezone=GMT%2B8&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
jdbc.username=root
jdbc.password=123456

# 要生成数据库设计表的表名, 多个表用 "," 连接, 不填则默认该数据库下所有表
custom.tableNames=example01,example02

# excel 文件输出目录, dir 为相对路径
custom.out.dir=/out
custom.out.fileName=example.xlsx

```



### 修改 application.properties

指定为你自定义的配置文件

![image-20221222160634152](https://typora-oss.yixihan.chat//img/202212221606223.png)



#### 配置介绍

```properties
# 启用的配置文件
enable.config=example.properties
```



### 运行程序

运行 `DatabaseDesignGenerator.java`

![image-20221222160903795](https://typora-oss.yixihan.chat//img/202212221609269.png)



### 查看运行结果

![image-20221222160954564](https://typora-oss.yixihan.chat//img/202212221609601.png)

![image-20221222161009559](https://typora-oss.yixihan.chat//img/202212221610773.png)

![image-20221222161034083](https://typora-oss.yixihan.chat//img/202212221610215.png)



## 常见问题

- 暂无
