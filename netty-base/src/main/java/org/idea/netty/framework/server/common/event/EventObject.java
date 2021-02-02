package org.idea.netty.framework.server.common.event;

/**
 * 事件通知参数
 *
 * @Author linhao
 * @Date created in 4:26 下午 2021/2/2
 */
public class EventObject<T> {


    private EventTypeEnum typeEnum;

    private T object;


    public EventObject(T object, EventTypeEnum eventTypeEnum) {
        this.typeEnum = eventTypeEnum;
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public EventTypeEnum getTypeEnum(){
        return typeEnum;
    }
}
