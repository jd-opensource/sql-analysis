package com.jd.sql.analysis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author huhaitao21
 *  ducc监听工具类
 *  15:34 2023/5/25
 **/
public class DuccMonitorUtil {

    private static Logger log = LoggerFactory.getLogger(DuccMonitorUtil.class);
    private static String duccConfig = "";

    /**
     * 启动监控
     * @param appName jone或者jdos应用名称
     * @param uri uri格式详解参见
     * @param moniterKey 存储sql替换配置的key
     */
    public static void start(String appName,String uri,String moniterKey){
        try{
            //todo 配置中心监听
//            //创建ConfiguratorManager 实例，有1个就可以
//            ConfiguratorManager configuratorManager = ConfiguratorManager.getInstance() ;
//            //设置appName，jone或者jdos部署可自动获取，无需配置
//            configuratorManager.setApplication(appName);
//
//            //resourceName是资源名，命名自定义，多个时不要重复
//            String resourceName = "sql_analysis_config";
//
//            //创建资源对象，此处直接使用ducc远程，Name属性很重要，下面会用到
//            Resource resource = new Resource(resourceName, uri);
//            //给配置管理器添加管理的资源
//            configuratorManager.addResource(resource);
//
//            //启动之后才可以获取配置
//            configuratorManager.start();
//
//            //获取配置 (获取指定配置源下指定配置)
//            Property property = configuratorManager.getProperty(resourceName, moniterKey);
//            log.info("sql analysis ducc moniterKey:" + property.getString());
//            duccConfig = property.getString();
//
//            //添加监听器，配置项维度的监听器
//            configuratorManager.addListener(new PropertyListener.CustomPropertyListener(moniterKey) {
//                @Override
//                public void onUpdate(Property property) {
//                    duccConfig = property.getString();
//                    log.info(JSON.toJSONString(property));
//                    SqlReplaceConfig.initConfig();
//                }
//            });
        }catch (Exception e){
            log.error("sql analysis ducc 监听启动失败");
        }

    }

    /**
     * 获取ducc配置
     * @return 分析对象
     */
    public static String getDuccConfig(){
        return duccConfig;
    }

}
