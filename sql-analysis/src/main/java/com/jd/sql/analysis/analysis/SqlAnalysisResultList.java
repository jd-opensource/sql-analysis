package com.jd.sql.analysis.analysis;

import java.util.List;

/**
 * @author huhaitao21
 *  sql 分析结果 集合
 *  20:41 2022/11/1
 **/
public class SqlAnalysisResultList {

    /**
     * 分析结果集合
     */
    private List<SqlAnalysisResult> resultList;

    public List<SqlAnalysisResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<SqlAnalysisResult> resultList) {
        this.resultList = resultList;
    }
}
