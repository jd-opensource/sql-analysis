package com.jd.sql.analysis.out;

import com.jd.sql.analysis.score.SqlScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huhaitao21
 *  分析结果存储到表
 *  17:24 2022/11/7
 **/
public class SqlScoreResultOutMysql implements SqlScoreResultOutService {

    private static Logger logger = LoggerFactory.getLogger(SqlScoreResultOutMysql.class);

    @Override
    public void outResult(SqlScoreResult sqlScoreResult) {
        //todo 待实现
    }
}
