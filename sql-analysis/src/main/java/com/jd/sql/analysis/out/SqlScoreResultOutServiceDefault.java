package com.jd.sql.analysis.out;

import com.jd.sql.analysis.score.SqlScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huhaitao21
 *  默认分析结果输出方式
 *  17:24 2022/11/7
 **/
public class SqlScoreResultOutServiceDefault implements SqlScoreResultOutService {

    private static Logger logger = LoggerFactory.getLogger(SqlScoreResultOutServiceDefault.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        if(sqlScoreResult==null){
            return;
        }
        if(sqlScoreResult.getNeedWarn()!=null && sqlScoreResult.getNeedWarn()){
            logger.error("sql analysis result score:{}",sqlScoreResult.getScore());
            if(sqlScoreResult.getAnalysisResults()!=null){
                sqlScoreResult.getAnalysisResults().forEach(result->{
                    logger.error("sql analysis result detail-reason:{},suggestion:{}",result.getReason(),result.getSuggestion());
                });
            }
        }
    }
}
