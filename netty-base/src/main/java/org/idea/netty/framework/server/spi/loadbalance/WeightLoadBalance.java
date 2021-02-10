package org.idea.netty.framework.server.spi.loadbalance;

import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.common.utils.RandomList;
import org.idea.netty.framework.server.config.Invocation;
import org.idea.netty.framework.server.spi.LoadBalance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加权的负载均衡器
 *
 * @Author linhao
 * @Date created in 5:34 下午 2021/2/2
 */
public class WeightLoadBalance implements LoadBalance {

    public static Map<String, URL[]> randomWeightMap = new ConcurrentHashMap<>();

    public static Map<String, Integer> lastIndexVisitMap = new ConcurrentHashMap<>();

    @Override
    public void doSelect(Invocation invocation) {
        URL[] weightArr = randomWeightMap.get(invocation.getServiceName());
        if (weightArr == null) {
            List<URL> urls = invocation.getUrls();
            Integer totalWeight = 0;
            for (URL url : urls) {
                //weight如果设置地过大，容易造成内存占用过高情况发生，所以weight统一限制最大大小应该为100
                Integer weight = Integer.valueOf(url.getParameters().get("weight"));
                totalWeight += weight;
            }
            weightArr = new URL[totalWeight];
            RandomList<URL> randomList = new RandomList(totalWeight);
            for (URL url : urls) {
                int weight = Integer.parseInt(url.getParameters().get("weight"));
                for (int i = 0; i < weight; i++) {
                    randomList.randomAdd(url);
                }
            }
            int len = randomList.getRandomList().size();
            for (int i = 0; i < len; i++) {
                URL url = randomList.getRandomList().get(i);
                weightArr[i] = url;
            }
            randomWeightMap.put(invocation.getServiceName(), weightArr);
        }
        Integer lastIndex = lastIndexVisitMap.get(invocation.getServiceName());
        if (lastIndex == null) {
            lastIndex = 0;
        }
        if (lastIndex >= weightArr.length) {
            lastIndex = 0;
        }
        URL referUrl = weightArr[lastIndex];
        lastIndex++;
        lastIndexVisitMap.put(invocation.getServiceName(), lastIndex);
        invocation.setReferUrl(referUrl);
    }


//    private void randomOffset(URL[] randomWeightArr, int currentWeight,URL currentUrl) {
//        Set r = new LinkedHashSet(currentWeight);
//        Random random = new Random();
//        while (r.size() < currentWeight ) {
//            int i = random.nextInt(currentWeight);
//            if(randomWeightArr[i]==null){
//                randomWeightArr[i] = currentUrl;
//                r.add(i);
//            }
//        }
//    }

    public static void main(String[] args) {
        Long i = 0L;
        Integer j = 0;
        System.out.println(i.equals(j));

        Integer k = 0;
        Integer p = 0;
        System.out.println(k.equals(p));
        System.out.println(i.intValue() == j.intValue());

    }
}
