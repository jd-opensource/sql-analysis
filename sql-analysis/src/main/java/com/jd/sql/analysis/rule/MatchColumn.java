package com.jd.sql.analysis.rule;

/**
 * @author huhaitao21
 *  匹配列
 *  15:50 2022/11/9
 **/
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
    private String column;

    MatchColumn(String column){
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
