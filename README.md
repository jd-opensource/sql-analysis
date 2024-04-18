Mybatis-SQL分析组件

# 背景

大促备战，最大的隐患项之一就是慢sql，带来的破坏性最大，也是日常工作中经常带来整个应用抖动的最大隐患，而且对sql好坏的评估有一定的技术要求，有一些缺乏经验或者因为不够仔细造成一个坏的sql成功走到了线上，等发现的时候要么是造成了线上影响、报警、或者后置的慢sql采集发现，这时候一般无法快速止损，需要修改代码上线、或者调整数据库索引。

核心痛点：

**1、无法提前发现慢sql，可能恶化为慢sql的语句**

**2、线上出现慢sql后，无法快速止损**



# 解决思路

**1、把问题解决在上线之前，最好的办法就是在测试阶段，甚至在开发阶段就发现一个sql的好坏**

**2、线上发现慢sql后除了改代码上线、调整数据库表索引的方式外，支持热更新的方式替换sql语句**。

部门内部，目前大部分数据库框架采用的mybatis，然后基于mybatis本身的实现机制中，开发一个mybatis组件，可以自动对运行的sql进行提取和分析，定制一套默认的分析规则，让sql在开发环境和测试环境执行的时候，就能够做初步的评估，把有问题的慢sql在这个阶段暴露出来；同时具备sql替换功能，在线上出现问题sql的时候，可以通过ducc配置快速完成对一个sql的在线替换，大大降低线上问题的止损时间。



# 开源方案调研

目前，主流的sql分析组件，核心功能主要放在了两个方向：1、慢sql的分析和优化建议  2、sql的优化重写功能，而且主要偏运维的辅助功能无法做到无侵入的和应用代码进行集成。也就无法实现我们的核心痛点，慢sql提前分析预警和动态sql替换。

![img](https://github.com/huht123/sql-analysis-img/blob/main/%E5%AF%B9%E6%AF%94%E5%9B%BE.png)



# 设计方案

**核心功能：SQL分析预警能力、SQL替换能力**



![img](https://github.com/huht123/sql-analysis-img/blob/main/%E8%AE%BE%E8%AE%A1%E5%9B%BE.png)



# 详细设计

主要分为8个功能模块

模块一：core 主要负责组件的接入到mybatis，以及其它模块的编排调用

模块二：config 主要负责组件配置信息的初始化

模块三：extrat 主要通过解析mybatis 相关对象，提取完整的待执行sql

模块四：analysis 主要拼接分析语句，执行explain分析语句并获取分析结果

模块五：rule sql分析规则的加载和初始化，支持自定义规则

目前默认规则（持续扩展）：

1、查询未匹配索引

2、匹配索引过滤效果较差

3、返回行数过多

4、使用了文件排序

模块六：score 基于分析结果和配置的评分规则进行匹配打分，优化建议组装

模块七：out 输出模块，对于输出结果进行输出，目前已error日志、MQ两种输出方式

模块八：replace替换模块，可以对sql语句基于ducc配置进行动态替换

# 使用方法

## 1、引入依赖jar包



```
<dependency>
    <groupId>io.github.huht123.sql-analysis</groupId>
    <artifactId>sql-analysis</artifactId>
    <version>1.0</version>
</dependency>
```

## 2、配置组件xml

```
<configuration>
    <plugins>
        <plugin interceptor="com.jd.sql.analysis.core.SqlAnalysisAspect" >
            <!-- 开启sql分析功能最简配置 -->
            <property name="analysisSwitch" value="true"/>

            <!-- 开启sql替换功能最简配置 -->
            <property name="sqlReplaceModelSwitch" value="true"/>
           
        </plugin>
    </plugins>
</configuration>
```

## 3、核心配置项

| 属性                  | 用途                                       | 是否必填 | 默认值             | 备注                        |
| --------------------- | ------------------------------------------ | -------- | ------------------ | --------------------------- |
| analysisSwitch        | 是否开启分析功能                           | 是       | false              |                             |
| onlyCheckOnce         | 是否对一个sqlid只分析一次                  | 非       | true               |                             |
| checkInterval         | 每个sqlid分析间隔                          | 非       | 300000毫秒         | onlyCheckOnce 为false才生效 |
| exceptSqlIds          | 需要过滤不分析的sqlid                      | 非       |                    |                             |
| sqlType               | 分析的sql类型                              | 非       | 默认select、update | 支持                        |
| scoreRuleLoadClass    | 评分规则加载器，用于扩展自定义规则         | 非       |                    |                             |
| outModel              | 默认输出方式                               | 非       | 默认值：LOG        | 支持LOG、MQ两种方式         |
| outputClass           | 评分结果输出类，用于扩展自定义结果输出方式 | 非       |                    |                             |
| sqlReplaceModelSwitch | sql替换模块是否开启                        | 非       | 默认 false         |                             |


## 4、实践使用方案

### 1、慢sql分析-日志输出+关键词告警

```
<configuration>
    <plugins>
        <plugin interceptor="com.jd.sql.analysis.core.SqlAnalysisAspect" >
            <property name="analysisSwitch" value="true"/>
        </plugin>
    </plugins>
</configuration>
```


### 2、慢sql分析-日志输出+mq输出+es存储+Kibana分析

```
<configuration>
    <plugins>
        <plugin interceptor="com.jd.sql.analysis.core.SqlAnalysisAspect" >
            <property name="analysisSwitch" value="true"/>
            <property name="outputClass" value="自定义输出类"/>
        </plugin>
    </plugins>
</configuration>
```
实现该接口，自定义输出方式（需要自己保证输出性能，可以采用异步队列）

    com.jd.sql.analysis.out.SqlScoreResultOutService


### 3、慢sql替换-配置动态更新sql语句

```
<configuration>
    <plugins>
        <plugin interceptor="com.jd.sql.analysis.core.SqlAnalysisAspect" >
            <property name="sqlReplaceModelSwitch" value="true"/>
        </plugin>
    </plugins>
</configuration>
```
可以集成自己环境的配置中心，通过如下方法或者映射map动态更新

    com.jd.sql.analysis.replace.SqlReplaceConfig.getSqlReplaceMap

注意：功能正式修复后，需去掉该配置，该功能仅供应急处理线上问题，不建议作为功能长期使用



# 性能测试

测试环境千次普通sql查询，每种场景进行了5次测试

未启用插件耗时：11108ms，10237ms，9482ms，7938ms，8196ms

开启sql分析耗时：16619ms，17333ms，16321ms，19057ms，18164ms

实际配置，只有首次执行或者间隔时间执行，单次影响10ms左右）

开启sql替换耗时：10642ms，8803ms，8353ms，8830ms，9170ms

基本无影响



# 适用场景

1、慢sql预防

2、线上问题止损



# 优势

1、核心优势：执行时分析sql，区别于传统的依赖sql执行耗时来评估慢sql，直接基于语法和索引进行前置分析，不仅能预防某些坏sql在上线后发现是慢sql，还能给出sql优化建议，可以大限度的避免线上产生慢sql。支持动态对线上sql进行替换，可以对线上问题快速止损。

2、性能：基于性能和不同的使用场景考虑，支持定制化配置，每个sql是否仅进行一次检查、或者按某个时间间隔进行配置。sql替换几乎无损耗。

3、扩展：基于后续sql评分规则的扩展、以及分析结果以不同的方式输出的考虑，支持评分规则、输出方式的自定义扩展。

4、成本：接入成本低，无代码侵入。


# 主要贡献者：
扈海涛（huhaitao21@jd.com)、杨超（yangchao341@jd.com）、张泽龙（zhangzelong10@jd.com）

# 欢迎共同改进和使用咨询
扈海涛（huhaitao21@jd.com)

