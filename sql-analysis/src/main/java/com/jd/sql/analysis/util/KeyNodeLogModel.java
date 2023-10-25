package com.jd.sql.analysis.util;

import lombok.Builder;

import java.util.Date;

/**
 * @Author huhaitao21
 * @Description 关键节点日志模型
 * @Date 16:45 2021/4/9
 **/
@Builder
public class KeyNodeLogModel {

    public KeyNodeLogModel(){

    }
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Date getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(Date businessTime) {
        this.businessTime = businessTime;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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
