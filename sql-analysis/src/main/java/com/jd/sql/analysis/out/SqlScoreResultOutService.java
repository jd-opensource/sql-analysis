package com.jd.sql.analysis.out;

import com.jd.sql.analysis.score.SqlScoreResult;

/**
 * @author huhaitao21
 *  sql评分结果输出
 *  12:14 2022/11/3
 **/
public interface SqlScoreResultOutService {

    /**
     * 输出分析结果
     * @param sqlScoreResult
     */
    void outResult(SqlScoreResult sqlScoreResult);
}
