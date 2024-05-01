package com.jd.sql.analysis.score;

import com.jd.sql.analysis.analysis.SqlAnalysisResult;
import com.jd.sql.analysis.analysis.SqlAnalysisResultList;
import com.jd.sql.analysis.rule.RulesEngineExecutor;
import com.jd.sql.analysis.util.GsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author huhaitao21
 * @Description 采用规则引擎计算评分实现方式
 * @Date 20:43 2022/11/1
 **/
public class SqlScoreServiceRulesEngine implements SqlScoreService {

    private static final Logger logger = LoggerFactory.getLogger(SqlScoreServiceRulesEngine.class);

    private static final Integer WARN_SCORE = 80;


    @Override
    public SqlScoreResult score(SqlAnalysisResultList sqlAnalysisResultList) {
        if (sqlAnalysisResultList == null || CollectionUtils.isEmpty(sqlAnalysisResultList.getResultList())) {
            return null;
        }
        //默认100分，扣分制
        Integer score = 100;
        SqlScoreResult scoreResult = new SqlScoreResult();

        List<SqlScoreResultDetail> analysisResults = new ArrayList<>();
        //遍历分析结果,匹配评分规则
        for (SqlAnalysisResult result : sqlAnalysisResultList.getResultList()) {
            List<SqlScoreResultDetail> detail = matchRuleEngine(result);
            if (CollectionUtils.isNotEmpty(detail)) {
                analysisResults.addAll(detail);
            }
        }

        //综合评分计算
        for (SqlScoreResultDetail detail : analysisResults) {
            score = score - detail.getScoreDeduction();
            if (score < 0) {
                //防止出现负分
                score = 0;
            }

            if (score < WARN_SCORE) {
                scoreResult.setNeedWarn(true);
            } else {
                scoreResult.setNeedWarn(false);
            }
        }
        scoreResult.setScore(score);
        scoreResult.setAnalysisResults(analysisResults);

        logger.info("sql analysis result = " + GsonUtil.bean2Json(scoreResult));
        return scoreResult;
    }

    private List<SqlScoreResultDetail> matchRuleEngine(SqlAnalysisResult result) {
        List<SqlScoreResultDetail> ret = RulesEngineExecutor.executeEngine(result);
        return ret;
    }
}