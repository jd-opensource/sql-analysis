package com.jd.sql.analysis.out;

import com.jd.sql.analysis.score.SqlScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author huhaitao21
 * @Description 分析结果存储到表
 * @Date 17:24 2022/11/7
 **/
public class SqlScoreResultOutMq implements SqlScoreResultOutService {

		private static final Logger logger = LoggerFactory.getLogger(SqlScoreResultOutMq.class);

		@Override
		public void outResult(SqlScoreResult sqlScoreResult) {
				if (sqlScoreResult == null) {
						return;
				}
				if (sqlScoreResult.getNeedWarn() != null && sqlScoreResult.getNeedWarn()) {
						//发送mq
						//todo 待替换为开源组件
						logger.error("sql analysis result sqlId:{}, score:{}", sqlScoreResult.getSqlId(), sqlScoreResult.getScore());
						if (sqlScoreResult.getAnalysisResults() != null) {
								sqlScoreResult.getAnalysisResults().forEach(result -> logger.error("sql analysis result detail-reason:{},suggestion:{}", result.getReason(), result.getSuggestion()));
						}
				}
		}

}
