package com.jd.sql.analysis.extract;

import com.jd.sql.analysis.config.SqlAnalysisConfig;
import com.jd.sql.analysis.util.GsonUtil;
import com.jd.sql.analysis.util.MetaObjectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

/**
 * @Author huhaitao21
 * @Description 待分析sql提取器
 * @Date 9:57 2022/11/7
 **/
public class SqlExtract {

    private static final Logger logger = LoggerFactory.getLogger(SqlExtract.class);

    /**
     * 记录sqlId-check time
     */
    private static final ConcurrentHashMap<String,Long> checkedIdMap = new ConcurrentHashMap<>();

    /**
     * 提取完整sql
     * @param statementHandler
     * @return
     */
    public static SqlExtractResult extract(StatementHandler statementHandler){

        //MetaObjectUtil 通过mybatis 反射工具类，从入参提取相关对象
        //提取 PreparedStatementHandler ，用于提取 MappedStatement
        MetaObject delegateMetaObject = MetaObjectUtil.forObject(statementHandler);
        if(delegateMetaObject.getValue("delegate")==null){
            logger.warn("sql analysis get delegate null error,{}", GsonUtil.bean2Json(statementHandler.getBoundSql()));
            return null;
        }
        if(!(delegateMetaObject.getValue("delegate") instanceof PreparedStatementHandler)){
            logger.info("sql analysis get delegate is not PreparedStatementHandler,{}", GsonUtil.bean2Json(statementHandler.getBoundSql()));
            return null;
        }
        PreparedStatementHandler preparedStatementHandler = (PreparedStatementHandler)delegateMetaObject.getValue("delegate");
        //提取 MappedStatement，用于组装完成带参数sql
        MetaObject metaObject = MetaObjectUtil.forObject(preparedStatementHandler);
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("mappedStatement");

        // 获取到节点的id,即sql语句的id
        String sqlId = mappedStatement.getId();
        String sqlType = mappedStatement.getSqlCommandType().name();
        logger.debug("sql analysis sqlId ={},sqlType={} " ,sqlId,sqlType);

        //判断是否需要分析
        if(!needAnalysis(sqlId,sqlType)){
            return null;
        }
        //记录检查时间
        checkedIdMap.put(sqlId,System.currentTimeMillis());

        // BoundSql就是封装myBatis最终产生的sql类
        Object parameterObject = statementHandler.getParameterHandler().getParameterObject();
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        // 获取节点的配置
        Configuration cOnfiguration= mappedStatement.getConfiguration();
        // 获取到最终的sql语句
        String sql = getSql(cOnfiguration, boundSql);
        logger.info("sql analysis sql = " + sql);

        SqlExtractResult result = new SqlExtractResult();
        result.setSqlId(sqlId);
        result.setSourceSql(sql);
        return result;
    }

    /**
     * 是否需要分析
     * @return
     */
    private static boolean  needAnalysis(String sqlId,String sqlType){
        //判断检查类型
        if(!SqlAnalysisConfig.getSqlType().contains(sqlType)){
            return false;
        }

        //判断 例外id
        if(SqlAnalysisConfig.getExceptSqlIds().contains(sqlId)){
            return false;
        }

        //onlyCheck判断
        if(SqlAnalysisConfig.getOnlyCheckOnce() && checkedIdMap.get(sqlId)!=null){
            return false;
        }

        //检查间隔判断
		    return checkedIdMap.get(sqlId) == null || (System.currentTimeMillis() - checkedIdMap.get(sqlId)) >= SqlAnalysisConfig.getCheckInterval();
    }




    /**
     * 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
     */
    private static String getSql(Configuration configuration, BoundSql boundSql){
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sql);
        return str.toString();
    }


    /**
     * 进行？的替换
     * @param configuration
     * @param boundSql
     * @return
     */
    private static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
     * @param obj
     * @return
     */
    private static String getParameterValue(Object obj)
    {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        }  else if (obj instanceof LocalDateTime) {
            value = "'" + Timestamp.valueOf((LocalDateTime) obj) + "'";
        } else if (obj instanceof LocalDate) {
            value = "'" + java.sql.Date.valueOf((LocalDate) obj) + "'";
        } else if (obj instanceof LocalTime) {
            value = "'" + Time.valueOf((LocalTime) obj) + "'";
        }  else{
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }


}
