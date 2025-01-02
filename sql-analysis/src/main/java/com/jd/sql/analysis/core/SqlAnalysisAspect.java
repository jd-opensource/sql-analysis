package com.jd.sql.analysis.core;


import com.jd.sql.analysis.analysis.SqlAnalysis;
import com.jd.sql.analysis.analysis.SqlAnalysisResultList;
import com.jd.sql.analysis.config.JmqConfig;
import com.jd.sql.analysis.config.SqlAnalysisConfig;
import com.jd.sql.analysis.extract.SqlExtract;
import com.jd.sql.analysis.extract.SqlExtractResult;
import com.jd.sql.analysis.out.OutModelEnum;
import com.jd.sql.analysis.out.SqlScoreResultOutMq;
import com.jd.sql.analysis.out.SqlScoreResultOutService;
import com.jd.sql.analysis.out.SqlScoreResultOutServiceDefault;
import com.jd.sql.analysis.replace.SqlReplace;
import com.jd.sql.analysis.replace.SqlReplaceConfig;
import com.jd.sql.analysis.rule.SqlScoreRuleLoader;
import com.jd.sql.analysis.rule.SqlScoreRuleLoaderRulesEngine;
import com.jd.sql.analysis.score.SqlScoreResult;
import com.jd.sql.analysis.score.SqlScoreService;
import com.jd.sql.analysis.score.SqlScoreServiceRulesEngine;
import com.jd.sql.analysis.util.GsonUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Properties;


/**
 * @Author huhaitao21
 * @Description sql分析切面类
 * @Date 22:47 2022/10/25
 **/
@Intercepts({@Signature(
    type = StatementHandler.class,
    method = "prepare",
    args = {Connection.class, Integer.class}
), @Signature(
    type = Executor.class,
    method = "update",
    args = {MappedStatement.class, Object.class}
),@Signature(
    type = Executor.class,
    method = "query",
    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class SqlAnalysisAspect implements Interceptor {

    Logger logger = LoggerFactory.getLogger(SqlAnalysisAspect.class);

    /**
     * 评分规则服务
     */
    private static SqlScoreService sqlScoreService = new SqlScoreServiceRulesEngine();

    /**
     * 评分结果输出服务
     */
    private static SqlScoreResultOutService sqlScoreResultOut = new SqlScoreResultOutServiceDefault();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object firstArg = invocation.getArgs()[0];

            if(SqlAnalysisConfig.getSqlReplaceModelSwitch()!=null && SqlAnalysisConfig.getSqlReplaceModelSwitch() && firstArg instanceof MappedStatement){
                //sql替换模块
                MappedStatement mappedStatement = (MappedStatement)firstArg;
                String replaceSql = SqlReplaceConfig.getReplaceSqlBySqlId(mappedStatement.getId());
                if(StringUtils.isNotBlank(replaceSql)){
                    SqlReplace.replace(invocation,replaceSql);
                }
            }else if(SqlAnalysisConfig.getAnalysisSwitch()  && firstArg instanceof Connection){
                //sql 分析模块
                //获取入参statement
                Object expectedStatementHandler = invocation.getTarget();
                while (Proxy.isProxyClass(expectedStatementHandler.getClass())) {
                    MetaObject metaObject = SystemMetaObject.forObject(expectedStatementHandler);
                    //fastReturn
                    if (BooleanUtils.isNotTrue(metaObject.hasGetter("h.target"))) {
                        logger.error("cant find mappedStatement h.get method");
                        break;
                    }
                    expectedStatementHandler = metaObject.getValue("h.target");
                }
                //failFast
                if (!(expectedStatementHandler instanceof StatementHandler)) {
                    logger.error("expectedStatementHandler not instanceof StatementHandler!");
                    return invocation.proceed();
                }
                StatementHandler statementHandler = (StatementHandler)expectedStatementHandler;

                //提取待执行的完整sql语句
                SqlExtractResult sqlExtractResult = SqlExtract.extract(statementHandler);
                if(sqlExtractResult!=null){
                    //对sql进行分析
                    Connection connection = (Connection)invocation.getArgs()[0];
                    SqlAnalysisResultList resultList  =  SqlAnalysis.analysis(sqlExtractResult,connection);

                    //对分析结果进行评估
                    SqlScoreResult sqlScoreResult = sqlScoreService.score(resultList);
                    if(sqlScoreResult!=null){
                        sqlScoreResult.setSqlId(sqlExtractResult.getSqlId());
                        sqlScoreResult.setSourceSql(sqlExtractResult.getSourceSql());

                        //输出评分结果
                        sqlScoreResultOut.outResult(sqlScoreResult);
                    }else{
                        logger.error("sql analysis score error {},{}", GsonUtil.bean2Json(resultList),GsonUtil.bean2Json(sqlExtractResult));
                    }
                }
            }
        }catch (Exception e) {
            logger.error("sql analysis error ",e);
        }
        // 执行完上面的任务后，不改变原有的sql执行过程
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //初始化配置
        SqlAnalysisConfig.init(properties);

        //初始化评分规则
        SqlScoreRuleLoader sqlScoreRuleLoader = new SqlScoreRuleLoaderRulesEngine();
        if(StringUtils.isNotBlank(SqlAnalysisConfig.getScoreRuleLoadClass())){
            try {
                sqlScoreRuleLoader = (SqlScoreRuleLoader)Class.forName(SqlAnalysisConfig.getScoreRuleLoadClass()).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("sql analysis init score mode error",e);
            }
        }
        boolean loadScoreRuleRes= sqlScoreRuleLoader.loadScoreRule();
        if(!loadScoreRuleRes){
            logger.error("sql analysis loadScoreRule exception");
        }
        //初始化输出服务
        //mq方式输出
        if(StringUtils.isNotBlank(SqlAnalysisConfig.getOutputModel()) && SqlAnalysisConfig.getOutputModel().toUpperCase().equals(OutModelEnum.MQ.getModelType())){
            try {
                boolean result = JmqConfig.initMqProducer();
                if(result){
                    SqlScoreResultOutService outServiceClass = new SqlScoreResultOutMq();
                    sqlScoreResultOut = outServiceClass;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("sql analysis init mq out mode error",e);
            }
        }
        //自定义方式输出
        if(StringUtils.isNotBlank(SqlAnalysisConfig.getOutputClass())){
            try {
                SqlScoreResultOutService outServiceClass = (SqlScoreResultOutService)Class.forName(SqlAnalysisConfig.getOutputClass()).newInstance();
                sqlScoreResultOut = outServiceClass;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("sql analysis init out mode error",e);
            }
        }
    }
}
