package com.jd.sql.analysis.util;

import lombok.Getter;

/**
 * @Author huhaitao21
 * @Description //TODO
 * @Date 20:06 2023/3/16
 **/
@Getter
public enum MysqlVersion {

		MYSQL_5_6("MYSQL_5.6"),
		MYSQL_5_7("MYSQL_5.7");


		private final String version;

		MysqlVersion(String version) {
				this.version = version;
		}

}
