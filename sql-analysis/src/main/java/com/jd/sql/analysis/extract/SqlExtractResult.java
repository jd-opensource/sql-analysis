package com.jd.sql.analysis.extract;

/**
 * @Author huhaitao21
 * @Description sql 提取结果
 * @Date 10:09 2022/11/7
 **/
public class SqlExtractResult {

    /**
     * 基于mybatis 配置的sql id
     */
    private String sqlId;

    /**
     * 待执行，原sql
     */
    private String sourceSql;


    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

}
