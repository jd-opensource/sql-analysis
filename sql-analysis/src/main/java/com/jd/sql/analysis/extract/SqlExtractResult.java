package com.jd.sql.analysis.extract;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author huhaitao21
 * @Description sql 提取结果
 * @Date 10:09 2022/11/7
 **/
@Setter
@Getter
public class SqlExtractResult {

    /**
     * 基于mybatis 配置的sql id
     */
    private String sqlId;

    /**
     * 待执行，原sql
     */
    private String sourceSql;


}
