package org.idea.netty.framework.server.spi.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linhao
 * @date created in 11:23 上午 2020/10/7
 */
public class ExtensionLoader {

    /**
     * 存储扩展spi的map，key是spi文件里面写入的key
     */
    private static Map<String, Class<?>> extensionClassMap = new ConcurrentHashMap<>();

    private static final String EXTENSION_LOADER_DIR_PREFIX = "META-INF/ietty/";

    public static  Map<String, Class<?>> getExtensionClassMap(){
        return extensionClassMap;
    }

    public void loadDirectory(Class clazz) throws IOException {
        synchronized (ExtensionLoader.class){
            String fileName = EXTENSION_LOADER_DIR_PREFIX + clazz.getName();
            ClassLoader classLoader = this.getClass().getClassLoader();
            Enumeration<URL> enumeration = classLoader.getResources(fileName);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.startsWith("#")){
                        continue;
                    }
                    String[] keyClassInstance = line.split("=");
                    try {
                        extensionClassMap.put(keyClassInstance[0],Class.forName(keyClassInstance[1],true,classLoader));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static <T>Object initClassInstance(String className) {
        if(extensionClassMap!=null && extensionClassMap.size()>0){
            try {
                return (T)extensionClassMap.get(className).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {


    }

}
