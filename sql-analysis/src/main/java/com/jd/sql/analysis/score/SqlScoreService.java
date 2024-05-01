package com.jd.sql.analysis.score;

import com.jd.sql.analysis.analysis.SqlAnalysisResultList;

/**
 * @Author huhaitao21
 * @Description sql分析结果 评分服务
 * @Date 20:41 2022/11/1
 **/
public interface SqlScoreService {

    /**
     * 计算sql评分
     * @param sqlAnalysisResutlDto
     * @return
     */
    SqlScoreResult score(SqlAnalysisResultList sqlAnalysisResutlDto);

}
