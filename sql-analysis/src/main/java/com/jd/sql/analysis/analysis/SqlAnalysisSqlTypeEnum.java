package com.jd.sql.analysis.analysis;

/**
 * @author huhaitao21
 *  sql 分析组件支持分析的sql类型
 *  19:36 2022/11/3
 **/
public enum SqlAnalysisSqlTypeEnum {

    SELECT("SELECT", "查询"),
    UPDATE("UPDATE", "更新"),
    INSERT("INSERT", "插入"),
    DELETE("DELETE", "删除");


    SqlAnalysisSqlTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * sql类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
