package org.idea.netty.framework.server.util;

import io.netty.util.internal.StringUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author linhao
 * @Date created in 3:56 下午 2021/1/1
 */
public class PropertiesUtils {

    private static Properties properties;

    private static String DEFAULT_PROPERTIES_FILE = "/Users/linhao/IdeaProjects/netty-frameworke/netty-base/src/main/resources/ietty.properties";

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
        return properties.getProperty(key);
    }
}
