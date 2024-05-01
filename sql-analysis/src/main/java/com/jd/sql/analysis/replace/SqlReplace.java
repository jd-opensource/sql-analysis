package com.jd.sql.analysis.replace;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @Author huhaitao21
 * @Description sql替换模块
 * @Date 14:25 2023/6/1
 **/
public class SqlReplace {

		private static final Logger logger = LoggerFactory.getLogger(SqlReplace.class);

		public static void replace(Invocation invocation, String newSql) {
				// 获取当前执行的SQL语句
				Object[] args = invocation.getArgs();
				MappedStatement mappedStatement = (MappedStatement) args[0];
				Object parameter = args[1];
				// 生成新sql
				BoundSql boundSql = mappedStatement.getBoundSql(parameter);
				BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), newSql, boundSql.getParameterMappings(), parameter);
				logger.debug("sql analysis - sql replace old:{}", boundSql.getSql());
				logger.debug("sql analysis - sql replace new:{}", newSql);

				boundSql.getParameterMappings().forEach((e) -> {
						String prop = e.getProperty();
						if (boundSql.hasAdditionalParameter(prop)) {
								newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
						}

				});

				// 把新的查询放到statement里
				MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
				args[0] = newMs;
		}


		/**
		 * 替换sql，生成新的 MappedStatement
		 *
		 * @param ms
		 * @param newSqlSource
		 * @return
		 */
		private static MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
				MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

				builder.resource(ms.getResource());
				builder.fetchSize(ms.getFetchSize());
				builder.statementType(ms.getStatementType());
				builder.keyGenerator(ms.getKeyGenerator());
				if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
						StringBuilder keyPropertiesBuilder = new StringBuilder();
						String[] keyProperties = ms.getKeyProperties();
						Arrays.stream(keyProperties).forEach(key -> keyPropertiesBuilder.append(key).append(","));
						keyPropertiesBuilder.delete(keyPropertiesBuilder.length() - 1, keyPropertiesBuilder.length());
						builder.keyProperty(keyPropertiesBuilder.toString());
				}
				builder.timeout(ms.getTimeout());
				builder.parameterMap(ms.getParameterMap());
				builder.resultMaps(ms.getResultMaps());
				builder.resultSetType(ms.getResultSetType());
				builder.cache(ms.getCache());
				builder.flushCacheRequired(ms.isFlushCacheRequired());
				builder.useCache(ms.isUseCache());

				return builder.build();
		}


		private static class BoundSqlSqlSource implements SqlSource {
				private final BoundSql boundSql;

				public BoundSqlSqlSource(BoundSql boundSql) {
						this.boundSql = boundSql;
				}

				@Override
				public BoundSql getBoundSql(Object parameterObject) {
						return boundSql;
				}
		}
}
