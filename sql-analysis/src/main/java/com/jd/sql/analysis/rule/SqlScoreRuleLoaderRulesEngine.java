package com.jd.sql.analysis.rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author huhaitao21
 * @Description 通过规则引擎方式加载评分规则
 * @Date 20:43 2022/11/1
 **/
public class SqlScoreRuleLoaderRulesEngine implements SqlScoreRuleLoader {

    private static Logger logger = LoggerFactory.getLogger(SqlScoreRuleLoaderRulesEngine.class);


    @Override
    public boolean loadScoreRule() {
        return RulesEngineExecutor.refresh();
    }

}
