package com.jd.sql.analysis.analysis;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author huhaitao21
 * @Description sql 分析结果 集合
 * @Date 20:41 2022/11/1
 **/
@Setter
@Getter
public class SqlAnalysisResultList {

    /**
     * 分析结果集合
     */
    private List<SqlAnalysisResult> resultList;

}
