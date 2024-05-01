package com.jd.sql.analysis.rule;

import lombok.Getter;

/**
 * @Author huhaitao21
 * @Description 匹配列
 * @Date 15:50 2022/11/9
 **/
@Getter
public enum MatchColumn {
		SELECT_TYPE("selectType"),
		TABLE("table"),
		PARTITIONS("partitions"),
		TYPE("type"),
		POSSIBLE_KEYS("possibleKeys"),
		KEY("key"),
		KEY_LEN("keyLen"),
		REF("ref"),
		ROWS("rows"),
		FILTERED("filtered"),
		EXTRA("extra");

		/**
		 * 匹配字段
		 */
		private final String column;

		MatchColumn(String column) {
				this.column = column;
		}

}
