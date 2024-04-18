package com.jd.sql.analysis.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author huhaitao21
 *  JSON转换工具类
 *  20:19 2023/10/10
 **/
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 对象、集合转json
     *
     * @param obj 对象
     * @return 分析对象 json
     */
    public static String bean2Json(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * json转对象
     *
     * @param jsonString json
     * @param objClass   对象类型
     * @param <T>        对象类型
     * @return 分析对象 对象
     */
    public static <T> T json2Bean(String jsonString, Class<T> objClass) {
        return GSON.fromJson(jsonString, objClass);
    }

}
