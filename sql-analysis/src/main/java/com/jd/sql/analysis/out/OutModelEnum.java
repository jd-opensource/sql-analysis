package com.jd.sql.analysis.out;

/**
 * @Author huhaitao21
 * @Description  分析结果输出模式
 * @Date 14:15 2023/2/8
 **/
public enum OutModelEnum {
    LOG("LOG", "日志方式输出"),
    MQ("MQ", "发送mq"),
    MYSQL("MYSQL", "mysql表存储");



    OutModelEnum(String modelType, String modelDesc) {
        this.modelType = modelType;
        this.modelDesc = modelDesc;
    }

    /**
     * 模式类型
     */
    private String modelType;

    /**
     * 模式描述
     */
    private String modelDesc;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }
}
