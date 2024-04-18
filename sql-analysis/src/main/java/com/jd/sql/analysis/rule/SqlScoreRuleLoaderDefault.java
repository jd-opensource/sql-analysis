package com.jd.sql.analysis.rule;

import com.jd.sql.analysis.config.SqlAnalysisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author huhaitao21
 *  评分规则管理实现
 *  20:43 2022/11/1
 **/
@Deprecated
public class SqlScoreRuleLoaderDefault implements SqlScoreRuleLoader {

    private static Logger logger = LoggerFactory.getLogger(SqlScoreRuleLoaderDefault.class);


    @Override
    public boolean loadScoreRule() {
        List<SqlScoreRule> ruleList = SqlAnalysisConfig.getRuleList();
        //无索引规则
        SqlScoreRule sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(true);
        sqlScoreRule.setMatchColumn(MatchColumn.TYPE);
        sqlScoreRule.setMatchType(MatchType.EQUAL);
        sqlScoreRule.setMatchValue("ALL");
        sqlScoreRule.setReason("不走索引");
        sqlScoreRule.setSuggestion("建议创建索引");
        sqlScoreRule.setScoreDeduction(40);
        ruleList.add(sqlScoreRule);

        //排序不走索引规则
        sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(false);
        sqlScoreRule.setMatchColumn(MatchColumn.EXTRA);
        sqlScoreRule.setMatchType(MatchType.CONTAIN);
        sqlScoreRule.setMatchValue("filesort");
        sqlScoreRule.setReason("排序不走索引");
        sqlScoreRule.setSuggestion("建议优化索引或者优化sql");
        sqlScoreRule.setScoreDeduction(20);
        ruleList.add(sqlScoreRule);

        //索引效果不佳规则
        sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(false);
        sqlScoreRule.setMatchColumn(MatchColumn.FILTERED);
        sqlScoreRule.setMatchType(MatchType.LESS);
        sqlScoreRule.setMatchValue("60");
        sqlScoreRule.setReason("索引过滤效果不佳");
        sqlScoreRule.setSuggestion("建议优化索引或者优化sql");
        sqlScoreRule.setScoreDeduction(20);
        ruleList.add(sqlScoreRule);

        //索引效果不佳规则
        sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(false);
        sqlScoreRule.setMatchColumn(MatchColumn.ROWS);
        sqlScoreRule.setMatchType(MatchType.GREATER);
        sqlScoreRule.setMatchValue("50000");
        sqlScoreRule.setReason("遍历行数过多");
        sqlScoreRule.setSuggestion("建议优化索引或者优化sql");
        sqlScoreRule.setScoreDeduction(40);
        ruleList.add(sqlScoreRule);

        //索引效果不佳规则
        sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(false);
        sqlScoreRule.setMatchColumn(MatchColumn.ROWS);
        sqlScoreRule.setMatchType(MatchType.GREATER);
        sqlScoreRule.setMatchValue("5000");
        sqlScoreRule.setReason("遍历行数过多");
        sqlScoreRule.setSuggestion("建议优化索引或者优化sql");
        sqlScoreRule.setScoreDeduction(10);
        ruleList.add(sqlScoreRule);

        //表内数据极少，甚至无数据 加分避免误报
        sqlScoreRule = new SqlScoreRule();
        sqlScoreRule.setStrict(false);
        sqlScoreRule.setMatchColumn(MatchColumn.ROWS);
        sqlScoreRule.setMatchType(MatchType.LESS);
        sqlScoreRule.setMatchValue("50");
        sqlScoreRule.setReason("遍历行数较少");
        sqlScoreRule.setSuggestion("暂不需要优化");
        sqlScoreRule.setScoreDeduction(-20);
        ruleList.add(sqlScoreRule);
        return true;
    }

}
