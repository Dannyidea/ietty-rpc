package org.idea.netty.framework.server.common.event;

/**
 * @Author idea
 * @Date created in 10:32 上午 2020/7/11
 */
public enum EventTypeEnum {

    NODE_UPDATE(1, "节点更新"),
    NODE_ADD(2,"节点新增"),
    NODE_DELETE(3,"节点删除");

    EventTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    int code;
    String des;


}
