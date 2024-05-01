package com.jd.sql.analysis.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @Author huhaitao21
 * @Description jmq相关配置
 * @Date 18:20 2023/2/9
 **/
public class JmqConfig {
    private static final Logger  logger = LoggerFactory.getLogger(JmqConfig.class);
    /**
     * 应用
     */
    private static String app;

    /**
     * 用户
     */
    private static String user;

    /**
     * 密码
     */
    private static String password;

    /**
     * mq地址
     */
    private static String address;

    /**
     * 发送topic
     */
    private static String topic;

    /**
     * 应用名称配置key
     */
    private static final String MQ_APP = "mqApp";

    /**
     * 用户配置key
     */
    private static final String MQ_USER = "mqUser";

    /**
     * 密码配置key
     */
    private static final String MQ_PASSWORD = "mqPassword";

    /**
     * mq地址配置key
     */
    private static final String MQ_ADDRESS = "mqAddress";

    /**
     * topic
     */
    private static final String MQ_TOPIC = "mqTopic";


    /**
     * 初始化配置
     * @param properties
     */
    public static void initConfig(Properties properties) {
        //检查参数 初始化参数
        boolean result = checkConfig(properties);
        if(result){
            app = properties.getProperty(MQ_APP);
            user = properties.getProperty(MQ_USER);
            password = properties.getProperty(MQ_PASSWORD);
            address = properties.getProperty(MQ_ADDRESS);
            topic = properties.getProperty(MQ_TOPIC);
        }
    }

    private static boolean checkConfig(Properties properties) {
        if(properties==null){
            return false;
        }
        if(StringUtils.isBlank(properties.getProperty(MQ_APP)) || StringUtils.isBlank(properties.getProperty(MQ_USER)) ||
                StringUtils.isBlank(properties.getProperty(MQ_PASSWORD)) || StringUtils.isBlank(properties.getProperty(MQ_ADDRESS))
                || StringUtils.isBlank(properties.getProperty(MQ_TOPIC))){
            return false;
        }

        return true;
    }

    /**
     * 启动生产者
     */
    public static boolean initMqProducer(){
        try{
            //todo 初始化生产者
            return true;
        }catch (Exception e){
            logger.error("sql analysis mq config init error");
            return false;
        }
    }

    public static String getApp() {
        return app;
    }

    public static void setApp(String app) {
        JmqConfig.app = app;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        JmqConfig.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        JmqConfig.password = password;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        JmqConfig.address = address;
    }

    public static String getTopic() {
        return topic;
    }

    public static void setTopic(String topic) {
        JmqConfig.topic = topic;
    }
}
