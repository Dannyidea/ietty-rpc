package org.idea.netty.framework.server.util;

import io.netty.util.internal.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author linhao
 * @Date created in 3:56 下午 2021/1/1
 */
public class PropertiesUtils {

    private static Properties properties;

    private static Map<String,String> propertiesMap = new ConcurrentHashMap<>();

    private static String DEFAULT_PROPERTIES_FILE = "/Users/linhao/IdeaProjects/netty-framework/netty-base/src/main/resources/ietty.properties";

    public static void putPropertiesValue(String key,String value){
        propertiesMap.put(key,value);
    }

    static {
        properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesStr(String key) {
        if (properties == null) {
            return null;
        }
        if(StringUtils.isEmpty(key)){
            return null;
        }
        if(!propertiesMap.containsKey(key)){
            String value =  properties.getProperty(key);
            propertiesMap.put(key,value);
        }
        return String.valueOf(propertiesMap.get(key));
    }

    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static Integer getPropertiesInteger(String key) {
        if (properties == null) {
            return null;
        }
        if(StringUtils.isEmpty(key)){
            return null;
        }
        if(!propertiesMap.containsKey(key)){
            String value =  properties.getProperty(key);
            propertiesMap.put(key,value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }
}
