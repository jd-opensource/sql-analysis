package com.jd.sql.analysis.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author huhaitao21
 * @Description //TODO
 * @Date 20:06 2023/3/16
 **/
@Setter
@Getter
public enum MysqlVersion {

    MYSQL_5_6("MYSQL_5.6"),
    MYSQL_5_7( "MYSQL_5.7");


    MysqlVersion(String version) {
        this.version = version;
    }

    private final String version;

}
