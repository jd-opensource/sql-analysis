package com.jd.sql.analysis.rule;

import lombok.Getter;

/**
 * @Author huhaitao21
 * @Description 匹配类型
 * @Date 15:50 2022/11/9
 **/
@Getter
public enum MatchType {
		EQUAL("等于"),
		GREATER("大于"),
		LESS("小于"),
		CONTAIN("包含");

		/**
		 * 匹配类型
		 */
		private final String type;

		MatchType(String type) {
				this.type = type;
		}

}
