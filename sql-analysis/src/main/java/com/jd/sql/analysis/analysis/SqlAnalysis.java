package com.jd.sql.analysis.analysis;

import com.jd.sql.analysis.extract.SqlExtractResult;
import com.jd.sql.analysis.util.GsonUtil;
import com.jd.sql.analysis.util.MysqlVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author huhaitao21
 * @Description sql 分析模块
 * @Date 10:55 2022/11/7
 **/
public class SqlAnalysis {

		private static final Logger logger = LoggerFactory.getLogger(SqlAnalysis.class);

		/**
		 * mysql 版本标识
		 */
		private static String mysqlVersion;

		/**
		 * sql 分析
		 *
		 * @return
		 */
		public static SqlAnalysisResultList analysis(SqlExtractResult sqlExtractResult, Connection connection) {
				if (sqlExtractResult == null || connection == null) {
						return null;
				}
				String sourceSql = sqlExtractResult.getSourceSql();


				List<SqlAnalysisResult> resultList = new ArrayList<>();
				SqlAnalysisResult sqlAnalysisResutlDto = null;

				//包装分析语句
				String analysisSql = getAnalysisSql(sourceSql);
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
						pstmt = connection.prepareStatement(analysisSql);
						rs = pstmt.executeQuery();
						while (rs.next()) {
								sqlAnalysisResutlDto = convertSqlAnalysisResultDto(rs);
								resultList.add(sqlAnalysisResutlDto);
						}
				} catch (SQLException e) {
						e.printStackTrace();
				} finally {
						try {
								if (rs != null) {
										rs.close();
								}
								if (pstmt != null) {
										pstmt.close();
								}
						} catch (SQLException e) {
								e.printStackTrace();
						}
				}
				logger.info("sql analysis result = {}", GsonUtil.bean2Json(sqlAnalysisResutlDto));
				SqlAnalysisResultList sqlAnalysisResultList = new SqlAnalysisResultList();
				sqlAnalysisResultList.setResultList(resultList);
				return sqlAnalysisResultList;
		}


		/**
		 * 分析结果转换 分析结果dto
		 *
		 * @param resultSet
		 * @return
		 */
		private static SqlAnalysisResult convertSqlAnalysisResultDto(ResultSet resultSet) {
				SqlAnalysisResult sqlAnalysisResult = new SqlAnalysisResult();
				if (resultSet == null) {
						return null;
				}
				try {
						if (StringUtils.isBlank(mysqlVersion)) {
								mysqlVersion = getMysqlVersion(resultSet);
						}
						Long id = resultSet.getLong("id");
						String selectType = resultSet.getString("select_type");
						String table = resultSet.getString("table");
						String type = resultSet.getString("type");
						String possibleKeys = resultSet.getString("possible_keys");
						String key = resultSet.getString("key");
						String keyLen = resultSet.getString("key_len");
						String ref = resultSet.getString("ref");
						String rows = resultSet.getString("rows");
						String extra = resultSet.getString("Extra");

						sqlAnalysisResult.setId(id);
						sqlAnalysisResult.setSelectType(selectType);
						sqlAnalysisResult.setTable(table);
						sqlAnalysisResult.setType(type);
						sqlAnalysisResult.setPossibleKeys(possibleKeys);
						sqlAnalysisResult.setKey(key);
						sqlAnalysisResult.setKeyLen(keyLen);
						sqlAnalysisResult.setRef(ref);
						sqlAnalysisResult.setRows(rows);
						sqlAnalysisResult.setExtra(extra);
						if (mysqlVersion.equals(MysqlVersion.MYSQL_5_7.getVersion())) {
								Double filtered = resultSet.getDouble("filtered");
								String partitions = resultSet.getString("partitions");
								sqlAnalysisResult.setPartitions(partitions);
								sqlAnalysisResult.setFiltered(filtered);
						}

				} catch (SQLException e) {
						logger.error("sql analysis convert error", e);
						e.printStackTrace();
				}
				return sqlAnalysisResult;
		}

		/**
		 * 获取sql分析语句
		 *
		 * @param sql 拦截前的sql
		 * @return
		 */
		private static String getAnalysisSql(String sql) {
				sql = "explain " + sql;
				return sql;
		}

		public static String getMysqlVersion(ResultSet rs) {
				//根据返回字段数识别5.6 或者 5.7以上版本
				String mysqlVersion = MysqlVersion.MYSQL_5_6.getVersion();
				try {
						int columnCount = rs.getMetaData().getColumnCount();
						if (columnCount > 10) {
								mysqlVersion = MysqlVersion.MYSQL_5_7.getVersion();
						}
				} catch (Exception e) {
						logger.error("sql analysis 获取mysql版本异常", e);
				}
				return mysqlVersion;
		}

		/**
		 * 判断查询结果集中是否存在某列
		 *
		 * @param rs         查询结果集
		 * @param columnName 列名
		 * @return true 存在; false 不存咋
		 */
		public boolean isExistColumn(ResultSet rs, String columnName) {
				try {
						if (rs.findColumn(columnName) > 0) {
								return true;
						}
				} catch (SQLException e) {
						return false;
				}

				return false;
		}
}
