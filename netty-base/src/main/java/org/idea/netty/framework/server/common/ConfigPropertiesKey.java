package org.idea.netty.framework.server.common;

/**
 * @Author linhao
 * @Date created in 4:19 下午 2021/1/1
 */
public class ConfigPropertiesKey {

    public static final String REGISTER_ADDRESS_KEY = "ietty.register.address";

    public static final String REGISTER_ADDRESS_PORT_KEY = "ietty.register.port";

    public static final String IETTY_APPLICATION_VERSION = "ietty.application.version";

    public static final String IETTY_APPLICATION_NAME = "ietty.application.name";

    public static final String IETTY_PROTOCOL_NAME = "ietty.protocol.name";

    public static final String IETTY_PROTOCOL_HOST = "ietty.protocol.host";

    public static final String IETTY_PROTOCOL_PORT = "ietty.protocol.port";

    public static final String LOCAL_URL_STORE_LOCATION = "ietty.url.store.location";

    public static final String LOCAL_URL_CLIENT_STORE_LOCATION = "ietty.url.client.store.location";

    public static final String ROOT_PATH = "/ietty";

    public enum NODE_EVENT_TYPE {
        NODE_UPDATED("NODE_UPDATED","节点更新"),
        NODE_ADDED("NODE_ADDED","节点新增");
        String eventName;
        String desc;

        NODE_EVENT_TYPE(String eventName, String desc) {
            this.eventName = eventName;
            this.desc = desc;
        }
    }
}
