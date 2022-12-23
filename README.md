<div align="center">
  <div><h1>数据库设计表自动生成器</h1></div>
  <div>
    <img
      src="https://img.shields.io/badge/Hutool-5.8.10-blue.svg"
      alt="Coverage Status"
    />
    <img
      src="https://img.shields.io/badge/poi-5.2.2-blue.svg"
      alt="Downloads"
    />
    <img
      src="https://visitor-badge.glitch.me/badge?page_id=yixihan.database-design-generator&left_color=green&right_color=red"
    />
  </div>
</div>
<br>

## 用途
用于自动生成数据库设计表, 以 excel 文件形式展示


## 使用教程

### 在 resource 目录下新建配置文件

新建 xxx.properties 文件, 内容可参考 [example.properties](src/main/resources/config/example.properties)

![image-202212231024775](https://typora-oss.yixihan.chat//img/202212231024775.png)



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
custom.out.dir=out
custom.out.fileName=example.xlsx

```



### 修改 application.properties

将 [application.properties](src/main/resources/application.properties) 中启用的配置文件指定为你自定义的配置文件

![image-20221222160634152](https://typora-oss.yixihan.chat//img/202212221606223.png)



#### 配置介绍

```properties
# 启用的配置文件
enable.config=example.properties
```



### 运行程序

运行 `DatabaseDesignGenerator.java`

![image-202212231021771](https://typora-oss.yixihan.chat//img/202212231021771.png)



### 查看运行结果

![image-20221222160954564](https://typora-oss.yixihan.chat//img/202212221609601.png)

![image-20221222161009559](https://typora-oss.yixihan.chat//img/202212221610773.png)

![image-20221222161034083](https://typora-oss.yixihan.chat//img/202212221610215.png)



## 常见问题

- 一般常见问题请见log输出
- 其余问题请提issue
