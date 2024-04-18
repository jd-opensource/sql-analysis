package com.jd.sql.analysis.score;

import com.jd.sql.analysis.analysis.SqlAnalysisResultList;

/**
 * @author huhaitao21
 *  sql分析结果 评分服务
 *  20:41 2022/11/1
 **/
public interface SqlScoreService {

    /**
     * 计算sql评分
     * @param sqlAnalysisResutlDto
     * @return 分析对象
     */
    public SqlScoreResult score(SqlAnalysisResultList sqlAnalysisResutlDto);

}
