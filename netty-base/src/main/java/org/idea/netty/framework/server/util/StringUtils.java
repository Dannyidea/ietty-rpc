package org.idea.netty.framework.server.util;

import java.util.List;

/**
 * @author linhao
 * @date created in 6:57 下午 2020/10/9
 */
public class StringUtils {

    public static String EMPTY_STR = "";

    public static boolean isEmpty(String str){
        return str==null || str.length()==0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isStringArrEmpty(String[] arr){
        return arr==null || arr.length==0;
    }

    public static boolean isNotStringArrEmpty(String[] arr){
        return !isStringArrEmpty(arr);
    }
}
