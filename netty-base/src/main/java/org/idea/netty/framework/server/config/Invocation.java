package org.idea.netty.framework.server.config;

import com.alibaba.fastjson.JSON;
import org.idea.netty.framework.server.common.URL;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 更加详细的调用参数部分 会被存储到body字节数组里面
 *
 * @author linhao
 * @date created in 4:24 下午 2020/10/8
 */
public class Invocation implements Serializable {

    private static final long serialVersionUID = -3890786306776677106L;

    private Class serviceClass;

    private String serviceVersion;

    private String serviceName;

    private String methodName;

    private String[] methodParameterTypes;

    private Object[] arguments;

    private Map<String, Object> attachments;

    private List<URL> urls;

    private URL referUrl;

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getMethodParameterTypes() {
        return methodParameterTypes;
    }

    public void setMethodParameterTypes(String[] methodParameterTypes) {
        this.methodParameterTypes = methodParameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public byte[] toByteArray() {
        return JSON.toJSON(this).toString().getBytes();
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public URL getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(URL referUrl) {
        this.referUrl = referUrl;
    }
}
