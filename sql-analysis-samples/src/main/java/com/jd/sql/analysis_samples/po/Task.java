package com.jd.sql.analysis_samples.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * task
 * @author 
 */
@Data
public class Task implements Serializable {
    /**
     * 主键编码
     */
    private Long id;


    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}