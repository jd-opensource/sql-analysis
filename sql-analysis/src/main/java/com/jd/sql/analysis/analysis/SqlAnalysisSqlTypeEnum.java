package com.jd.sql.analysis.analysis;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author huhaitao21
 * @Description sql 分析组件支持分析的sql类型
 * @Date 19:36 2022/11/3
 **/
@Setter
@Getter
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
    private final String type;

    /**
     * 描述
     */
    private final String description;


}
