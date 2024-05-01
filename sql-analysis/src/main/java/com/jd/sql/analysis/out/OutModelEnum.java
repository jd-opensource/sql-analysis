package com.jd.sql.analysis.out;

import lombok.Getter;

/**
 * @Author huhaitao21
 * @Description 分析结果输出模式
 * @Date 14:15 2023/2/8
 **/
@Getter
public enum OutModelEnum {
		LOG("LOG", "日志方式输出"),
		MQ("MQ", "发送mq"),
		MYSQL("MYSQL", "mysql表存储");


		/**
		 * 模式类型
		 */
		private final String modelType;
		/**
		 * 模式描述
		 */
		private final String modelDesc;

		OutModelEnum(String modelType, String modelDesc) {
				this.modelType = modelType;
				this.modelDesc = modelDesc;
		}

}
