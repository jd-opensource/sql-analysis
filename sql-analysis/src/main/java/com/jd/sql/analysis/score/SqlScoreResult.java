package com.jd.sql.analysis.score;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author huhaitao21
 * @Description sql 评分结果
 * @Date 18:33 2022/11/2
 **/
@Setter
@Getter
public class SqlScoreResult {

    /**
     * sql id
     */
    private String sqlId;

    /**
     * 执行的原始sql
     */
    private String sourceSql;

    /**
     * 是否需要警告
     */
    private Boolean needWarn;

    /**
     * 综合评分
     */
    private Integer score;

    /**
     * 分析结果明细
     */
    List<SqlScoreResultDetail> analysisResults;

}
