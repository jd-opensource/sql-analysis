package com.jd.sql.analysis.rule;

import com.jd.sql.analysis.analysis.SqlAnalysisResult;
import com.jd.sql.analysis.score.SqlScoreResultDetail;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则引擎执行器
 * @author yangchao341
 */
public class RulesEngineExecutor {

    private static Logger logger = LoggerFactory.getLogger(RulesEngineExecutor.class);

    public final static RulesEngine rulesEngine = new DefaultRulesEngine();
    private static Rules rules;

    /**
     * 匹配规则-评分结果映射关系
     */
    private static ConcurrentHashMap<String,SqlScoreResultDetail> scoreMap;


    public static boolean refresh(){
        Properties properties = new Properties();
        try (InputStream inputStream = RulesEngineExecutor.class.getClassLoader().getResourceAsStream("sql-analysis-rule-config.properties");
            InputStreamReader reader = new InputStreamReader(inputStream)) {
            properties.load(reader);
        } catch (IOException e) {
            logger.error("规则引擎配置文件加载失败", e);
            return false;
        }
        Map<String,MVELRule> ruleMap = new HashMap<String,MVELRule>();
        ConcurrentHashMap<String,SqlScoreResultDetail> innerScoreMap = new ConcurrentHashMap<String,SqlScoreResultDetail>();
        properties.forEach((key, value) -> {
            String[] keyArr = StringUtils.split(key.toString(),'.');
            if(keyArr.length >= 2){
               MVELRule rule = ruleMap.getOrDefault(keyArr[0],new MVELRule().name(keyArr[0]).then("ret.add(\"" + keyArr[0] + "\")"));
               SqlScoreResultDetail score = innerScoreMap.getOrDefault(keyArr[0],new SqlScoreResultDetail());
               switch (RuleFieldEnum.valueOf(keyArr[1].toUpperCase())){
                   case CONDITION: rule.when(value.toString());break;
                   case ACTION: rule.then(value.toString());break;
                   case NAME: rule.name(value.toString());break;
                   case DESCRIPTION: rule.description(value.toString());break;
                   case PRIORITY: rule.priority(NumberUtils.toInt(value.toString()));break;
                   case SCORE: score.setScoreDeduction(NumberUtils.toInt(value.toString()));break;
                   case REASON: score.setReason(value.toString());break;
                   case SUGGESTION: score.setSuggestion(value.toString());break;
                   case STRICT: score.setStrict(Boolean.valueOf(value.toString()));break;
               }
               ruleMap.put(keyArr[0],rule);
               innerScoreMap.put(keyArr[0],score);
            }
        });
        Rules newRules = new Rules();
        for(Map.Entry<String,MVELRule> ruleEntry : ruleMap.entrySet()){
            newRules.register(ruleEntry.getValue());
        }
        rules = newRules;
        scoreMap = innerScoreMap;

        return true;
    }

    public static List<SqlScoreResultDetail> executeEngine(SqlAnalysisResult sqlAnalysisResult) {
        ArrayList<SqlScoreResultDetail> retList = new ArrayList<SqlScoreResultDetail>();
        ArrayList<String> ret = new ArrayList<String>();
        // 创建事实
        Facts facts = new Facts();
        facts.put("param", sqlAnalysisResult);
        facts.put("ret", ret);
        // 执行规则
        rulesEngine.fire(rules, facts);
        ret = facts.get("ret");
        for(String item : ret){
            if(scoreMap.get(item) != null){
                retList.add(scoreMap.get(item));
            }
        }
        return retList;
    }
}
