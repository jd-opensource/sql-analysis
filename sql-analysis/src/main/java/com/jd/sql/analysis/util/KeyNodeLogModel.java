package com.jd.sql.analysis.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author huhaitao21
 * @Description 关键节点日志模型
 * @Date 16:45 2021/4/9
 **/
@Setter
@Getter
@Builder
public class KeyNodeLogModel {

		/**
		 * 业务id
		 */
		private String businessId;
		/**
		 * 业务模块名称
		 */
		private String moduleName;
		/**
		 * 节点名称
		 */
		private String nodeName;
		/**
		 * 业务时间
		 */
		private Date businessTime;
		/**
		 * 日志时间
		 */
		private Date logTime;
		/**
		 * 描述
		 */
		private String describe;

		public KeyNodeLogModel() {

		}

		public KeyNodeLogModel(String businessId, String moduleName, String nodeName, Date businessTime, Date logTime, String describe) {
				this.businessId = businessId;
				this.moduleName = moduleName;
				this.nodeName = nodeName;
				this.businessTime = businessTime;
				this.logTime = logTime;
				this.describe = describe;
		}
}
