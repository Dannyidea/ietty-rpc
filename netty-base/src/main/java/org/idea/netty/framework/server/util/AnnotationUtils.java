package org.idea.netty.framework.server.util;

import org.idea.netty.framework.server.common.Service;
import org.idea.netty.framework.server.config.ProtocolConfig;
import org.idea.netty.framework.server.config.ServiceConfig;
import org.idea.netty.framework.server.test.service.Test;
import org.idea.netty.framework.server.test.service.impl.TestImpl;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author linhao
 * @date created in 11:14 下午 2020/10/9
 */
public class AnnotationUtils {


    /**
     * 获取当前项目中包含指定注解的类
     *
     * @param basePack
     * @return
     */
    public static Set<ServiceConfig> getServiceConfigByAnnotation(Class annotationClass, String basePack) {
        List<Class> classList = getClassFromPackage(basePack);
        Set<ServiceConfig> serviceConfigHashSet = new HashSet<>();
        for (Class aClass : classList) {
            if (aClass.isAnnotationPresent(annotationClass)) {
                Service service = (Service) aClass.getAnnotation(Service.class);
                ServiceConfig serviceConfig = new ServiceConfig();
                //父类接口
                Class<?> clazz[] = aClass.getInterfaces();
                serviceConfig.setInterfaceClass(clazz[0]);
                serviceConfig.setInterfaceImplClass(aClass);
                serviceConfig.setInterfaceName(service.interfaceName());
                serviceConfig.setServiceName(clazz[0].getName());
                ProtocolConfig protocolConfig = new ProtocolConfig();
                //todo
                protocolConfig.setPort(9090);
                protocolConfig.setName("ietty");
                protocolConfig.setHost("www.idea.com");

                if (service.delay() > 0) {
                    serviceConfig.setDelay(service.delay());
                }
                serviceConfig.setProtocolConfig(protocolConfig);
                List<String> methodList = new ArrayList<>();
                Class[] classes = aClass.getInterfaces();
                Class interfaceClazz = classes[0];
                Method[] methods = interfaceClazz.getDeclaredMethods();
                for (Method method : methods) {
                    methodList.add(method.getName());
                }
                String[] methodArr = new String[methodList.size()];
                for (int i = 0; i < methodList.size(); i++) {
                    methodArr[i] = methodList.get(i);
                }
                serviceConfig.setMethodNames(methodArr);
                serviceConfig.setFilter(service.filter());
                serviceConfigHashSet.add(serviceConfig);
            }
        }
        return serviceConfigHashSet;
    }


    /**
     * 获得包下面的所有的class
     *
     * @param pack package完整名称
     * @return List包含所有class的实例
     */
    private static List<Class> getClassFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    //todo
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     *
     * @param packageName package名称
     * @param filePath    package对应的路径
     * @param recursive   是否查找子package
     * @param clazzs      找到class以后存放的集合
     */
    private static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
                boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Method[] methods = Test.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }
}
