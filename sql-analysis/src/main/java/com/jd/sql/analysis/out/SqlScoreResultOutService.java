package com.jd.sql.analysis.out;

import com.jd.sql.analysis.score.SqlScoreResult;

/**
 * @Author huhaitao21
 * @Description sql评分结果输出
 * @Date 12:14 2022/11/3
 **/
public interface SqlScoreResultOutService {

    /**
     * 输出分析结果
     * @param sqlScoreResult
     */
    void outResult(SqlScoreResult sqlScoreResult);
}
