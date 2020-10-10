package org.idea.netty.framework.server.util;

import java.util.List;

/**
 * @author linhao
 * @date created in 6:57 下午 2020/10/9
 */
public class CollectionUtils {

    public static boolean isEmpty(List list){
        return list==null && list.size()==0;
    }
}
