package org.idea.netty.framework.server.util;

import java.util.List;
import java.util.Map;

/**
 * @author linhao
 * @date created in 6:57 下午 2020/10/9
 */
public class CollectionUtils {

    public static boolean isEmpty(List list){
        return list==null || list.size()==0;
    }

    public static boolean isMapNotEmpty(Map map){
        return !isMapEmpty(map);
    }

    public static boolean isMapEmpty(Map map){
        return map==null || map.size()==0;
    }
}
