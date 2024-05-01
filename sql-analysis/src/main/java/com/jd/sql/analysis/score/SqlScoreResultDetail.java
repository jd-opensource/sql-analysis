package com.jd.sql.analysis.score;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author huhaitao21
 * @Description sql 评分结果
 * @Date 18:33 2022/11/2
 **/
@Setter
@Getter
public class SqlScoreResultDetail {

		/**
		 * 减分值
		 */
		private Integer scoreDeduction;

		/**
		 * 原因
		 */
		private String reason;

		/**
		 * 建议
		 */
		private String suggestion;


		/**
		 * 是否严格规则，是的-直接触发警告，否-依赖综合评分进行警告（暂不使用）
		 */
		private Boolean strict;


}
