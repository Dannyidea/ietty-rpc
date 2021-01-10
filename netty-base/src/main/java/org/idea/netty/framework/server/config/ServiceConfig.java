package org.idea.netty.framework.server.config;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.StringUtil;
import lombok.ToString;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.register.Register;
import org.idea.netty.framework.server.register.RegisterFactory;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegister;
import org.idea.netty.framework.server.register.zookeeper.ZookeeperRegisterFactory;
import org.idea.netty.framework.server.spi.loader.ExtensionLoader;
import org.idea.netty.framework.server.util.StringUtils;


import javax.management.RuntimeErrorException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.idea.netty.framework.server.ServerApplication.isServerStart;

/**
 * @author linhao
 * @date created in 3:34 下午 2020/10/7
 */
@ToString
public class ServiceConfig {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * The interface name of the exported service
     */
    private String interfaceName;


    private String[] methodNames;

    /**
     * The interface class of the exported service
     */
    private Class<?> interfaceClass;

    /**
     * 具体实现类对象
     */
    private Class<?> interfaceImplClass;

    /**
     * 过滤器
     */
    private String filter;

    /**
     * 服务配置
     */
    private ApplicationConfig applicationConfig;

    /**
     * 协议配置
     */
    private ProtocolConfig protocolConfig;

    /**
     * 注册中心的配置
     */
    private RegisterConfig registerConfig;

    /**
     * 延迟发布的线程池
     */
    private static final ScheduledExecutorService DELAY_EXPORT_EXECUTOR = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("DubboServiceDelayExporter", true));


    /**
     * 是否延迟暴露服务 0 不延迟 其他 延迟时长（毫秒）
     */
    private int delay;


    /**
     * 暴露服务
     */
    public synchronized void export(URL url) {
        System.out.println("服务暴露");
        if(this.getRegisterConfig()==null){
            throw new RuntimeException("register config is null!");
        }
        String protocol = this.getRegisterConfig().getProtocol();
        url.setProtocol(protocol);
        if (this.delay > 0) {
            //延迟暴露服务
            DELAY_EXPORT_EXECUTOR.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("延迟发布");
                    doExport(url);
                }
            }, delay, TimeUnit.MILLISECONDS);
        } else {
            doExport(url);
        }
    }

    protected synchronized void doExport(URL url) {
        //在这里进行服务写入到zookeeper并且构建配置总线
        Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("serviceName", this.getServiceName());
        String[] methodNamesArr = this.getMethodNames();
        System.out.println(Arrays.toString(methodNamesArr));
        parameterMap.put("methods", Arrays.toString(methodNamesArr));
        parameterMap.put("port", String.valueOf(this.getProtocolConfig().getPort()));
        String registerAddress = this.getProtocolConfig().getHost();
        parameterMap.put("host", registerAddress);
        //默认初始化权重值
        parameterMap.put("weight", "100");
        url.setApplicationName(this.applicationConfig.getName());
        url.setParameters(parameterMap);
        url.setPath(this.getServiceName());
        String protocol = url.getProtocol();
        if (StringUtils.isNotEmpty(protocol)) {
            Class clazz = ExtensionLoader.getExtensionClassMap().get(protocol);
            try {
                RegisterFactory registerFactory = (RegisterFactory) clazz.newInstance();
                Register register = registerFactory.createRegister(url);
                register.register(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("没有协议");
        }
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public Class<?> getInterfaceImplClass() {
        return interfaceImplClass;
    }

    public void setInterfaceImplClass(Class<?> interfaceImplClass) {
        this.interfaceImplClass = interfaceImplClass;
    }

    public String[] getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String[] methodNames) {
        this.methodNames = methodNames;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public RegisterConfig getRegisterConfig() {
        return registerConfig;
    }

    public void setRegisterConfig(RegisterConfig registerConfig) {
        this.registerConfig = registerConfig;
    }
}
