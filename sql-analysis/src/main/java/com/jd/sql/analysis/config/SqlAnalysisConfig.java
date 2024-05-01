package com.jd.sql.analysis.config;

import com.jd.sql.analysis.analysis.SqlAnalysisSqlTypeEnum;
import com.jd.sql.analysis.rule.SqlScoreRule;
import com.jd.sql.analysis.util.DuccMonitorUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author huhaitao21
 * @Description sql 分析组件配置类
 * @Date 19:36 2022/11/3
 **/
public class SqlAnalysisConfig {


    private static final Logger logger = LoggerFactory.getLogger(SqlAnalysisConfig.class);

    /**
     * 分析开关，默认关闭
     */
    private static Boolean analysisSwitch = false;

    /**
     * 一个id 只检查一次，默认开启
     */
    private static Boolean onlyCheckOnce = true;

    /**
     * 两次检查间隔 默认 5分钟
     */
    private static Long checkInterval = 5 * 60 * 1000L;

    /**
     * 例外sql id，集合
     */
    private static List<String> exceptSqlIds = new ArrayList<>();

    /**
     * 进行分析的sql类型
     */
    private static List<String> sqlType = new ArrayList<>();


    /**
     * 评分规则加载类， 默认 com.jd.sql.analysis.rule.SqlScoreRuleLoaderDefault
     */
    private static String scoreRuleLoadClass;

    /**
     * 分析结果输出类，默认日志模式 com.jd.sql.analysis.out.SqlScoreResultOutServiceDefault
     */
    private static String outputModel;

    /**
     * 分析结果输出类，默认日志模式 com.jd.sql.analysis.out.SqlScoreResultOutServiceDefault
     */
    private static String outputClass;


    /**
     * 应用名称
     */
    private static String appName;

    /**
     * sqlReplaceModelSwitch
     */
    private static Boolean sqlReplaceModelSwitch;


    /**
     * 分析开关 配置key
     */
    private static final String ANALYSIS_SWITCH_KEY = "analysisSwitch";

    /**
     * 同一id是否只检查一次 配置key
     */
    private static final String ONLY_CHECK_ONCE = "onlyCheckOnce";

    /**
     * 检查间隔时间 配置key
     */
    private static final String CHECK_INTERVAL = "checkInterval";

    /**
     * 例外sql id 配置key,多个需要逗号分隔
     */
    private static final String EXCEPT_SQL_IDS_KEY = "exceptSqlIds";

    /**
     * 分析开关 配置key ,多个需要逗号分隔
     */
    private static final String SQL_TYPE_KEY = "sqlType";

    /**
     * 规则加载类 配置key
     */
    private static final String SCORE_RULE_LOAD_KEY = "scoreRuleLoadClass";

    /**
     * 评分输出类 配置key
     */
    private static final String OUTPUT_CLASS_KEY = "outputClass";

    /**
     * 输出模式 配置key
     */
    private static final String OUTPUT_MODEL_KEY = "outputModel";

    /**
     * 应用名称
     */
    private static final String APP_NAME = "appName";

    /**
     * 评分规则列表
     */
    private static List<SqlScoreRule> ruleList = new ArrayList<>();



    /**
     * 初始化配置
     * @param properties
     */
    public static void init(Properties properties){
        try{
            //加载 需要分析的sql类型
            if(StringUtils.isBlank(properties.getProperty(SQL_TYPE_KEY))){
                //默认 ，select 、update
                sqlType.add(SqlAnalysisSqlTypeEnum.SELECT.getType());
                sqlType.add(SqlAnalysisSqlTypeEnum.UPDATE.getType());
            }else{
                String[] sqlTypes = properties.getProperty(SQL_TYPE_KEY).split(",");
                CollectionUtils.addAll(sqlType,sqlTypes);
            }

            if(StringUtils.isNotBlank(properties.getProperty(ANALYSIS_SWITCH_KEY))){
                analysisSwitch = Boolean.valueOf(properties.getProperty(ANALYSIS_SWITCH_KEY));
            }
            if(StringUtils.isNotBlank(properties.getProperty(ONLY_CHECK_ONCE))){
                onlyCheckOnce = Boolean.valueOf(properties.getProperty(ONLY_CHECK_ONCE));
            }
            if(StringUtils.isNotBlank(properties.getProperty(CHECK_INTERVAL))){
                checkInterval = Long.valueOf(properties.getProperty(CHECK_INTERVAL));
            }
            if(StringUtils.isNotBlank(properties.getProperty(SCORE_RULE_LOAD_KEY))){
                scoreRuleLoadClass = properties.getProperty(SCORE_RULE_LOAD_KEY);
            }
            if(StringUtils.isNotBlank(properties.getProperty(OUTPUT_CLASS_KEY))){
                outputClass = properties.getProperty(OUTPUT_CLASS_KEY);
            }
            if(StringUtils.isNotBlank(properties.getProperty(OUTPUT_MODEL_KEY))){
                outputModel = properties.getProperty(OUTPUT_MODEL_KEY);
            }

            if(StringUtils.isNotBlank(properties.getProperty(EXCEPT_SQL_IDS_KEY))){
                String[] exceptIds = properties.getProperty(EXCEPT_SQL_IDS_KEY).split(",");
                CollectionUtils.addAll(exceptSqlIds,exceptIds);
            }

            if(StringUtils.isNotBlank(properties.getProperty(APP_NAME))){
                appName = properties.getProperty(APP_NAME);
            }else{
                appName = "default";
            }

            if(StringUtils.isNotBlank(properties.getProperty("sqlReplaceModelSwitch"))){
                sqlReplaceModelSwitch = Boolean.valueOf(properties.getProperty("sqlReplaceModelSwitch"));
            }

            //初始化mq配置
            JmqConfig.initConfig(properties);

            //初始化ducc配置
            if(sqlReplaceModelSwitch!=null && sqlReplaceModelSwitch && StringUtils.isNotBlank(properties.getProperty("duccAppName"))
                    && StringUtils.isNotBlank(properties.getProperty("duccUri"))
                    && StringUtils.isNotBlank(properties.getProperty("duccMonitorKey"))){
                DuccMonitorUtil.start(properties.getProperty("duccAppName"),properties.getProperty("duccUri"),properties.getProperty("duccMonitorKey"));
            }

        }catch (Exception e){
            logger.error("sql analysis config init error",e);
        }

    }

    public static Boolean getAnalysisSwitch() {
        return analysisSwitch;
    }

    public static void setAnalysisSwitch(Boolean analysisSwitch) {
        SqlAnalysisConfig.analysisSwitch = analysisSwitch;
    }

    public static Boolean getOnlyCheckOnce() {
        return onlyCheckOnce;
    }

    public static void setOnlyCheckOnce(Boolean onlyCheckOnce) {
        SqlAnalysisConfig.onlyCheckOnce = onlyCheckOnce;
    }

    public static Long getCheckInterval() {
        return checkInterval;
    }

    public static void setCheckInterval(Long checkInterval) {
        SqlAnalysisConfig.checkInterval = checkInterval;
    }

    public static List<String> getExceptSqlIds() {
        return exceptSqlIds;
    }

    public static void setExceptSqlIds(List<String> exceptSqlIds) {
        SqlAnalysisConfig.exceptSqlIds = exceptSqlIds;
    }

    public static List<String> getSqlType() {
        return sqlType;
    }

    public static void setSqlType(List<String> sqlType) {
        SqlAnalysisConfig.sqlType = sqlType;
    }

    public static String getScoreRuleLoadClass() {
        return scoreRuleLoadClass;
    }

    public static String getOutputClass() {
        return outputClass;
    }

    public static List<SqlScoreRule> getRuleList() {
        return ruleList;
    }

    public static void setRuleList(List<SqlScoreRule> ruleList) {
        SqlAnalysisConfig.ruleList = ruleList;
    }

    public static String getOutputModel() {
        return outputModel;
    }

    public static void setOutputModel(String outputModel) {
        SqlAnalysisConfig.outputModel = outputModel;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        SqlAnalysisConfig.appName = appName;
    }

    public static Boolean getSqlReplaceModelSwitch() {
        return sqlReplaceModelSwitch;
    }

    public static void setSqlReplaceModelSwitch(Boolean sqlReplaceModelSwitch) {
        SqlAnalysisConfig.sqlReplaceModelSwitch = sqlReplaceModelSwitch;
    }
}
