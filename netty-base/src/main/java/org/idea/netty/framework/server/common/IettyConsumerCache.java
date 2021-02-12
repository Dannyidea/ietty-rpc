package org.idea.netty.framework.server.common;

import org.idea.netty.framework.server.rpc.RpcData;
import org.idea.netty.framework.server.rpc.consumer.RpcReqData;
import org.idea.netty.framework.server.rpc.provider.RpcRespData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 专门用于消费端的缓存数组
 *
 * @Author linhao
 * @Date created in 4:33 下午 2021/2/11
 */
public class IettyConsumerCache {

    public static AtomicLong SESSION_ID = new AtomicLong(0);

    public static Map<Long, RpcRespData> CLIENT_RESP_MAP = new ConcurrentHashMap<>();


}
