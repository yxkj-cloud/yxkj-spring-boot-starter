# yxkj-spring-boot-starter

yxkj的SpringBoot自动装配器

# 快速开始

## 1. 添加依赖

```xml
<dependency>
    <groupId>com.yxkj</groupId>
    <artifactId>yxkj-spring-boot-starter</artifactId>
    <version>0.0.1.RELEASE</version>
</dependency>
```
## 2. 配置文件

```yaml
## 自动配置Yxkj的多数据源
yxkj:
  dds:
    datasource-a:
      url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    datasource-b:
      url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    default-ds: datasource-a
```

## 3. 使用
```java
    @Autowired
    private YxkjDds yxkjDds;
```
