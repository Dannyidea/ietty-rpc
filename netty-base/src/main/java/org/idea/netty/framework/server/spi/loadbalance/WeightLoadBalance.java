package org.idea.netty.framework.server.spi.loadbalance;

import org.idea.netty.framework.server.common.URL;
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

    private Map<String, URL[]> randomWeightMap = new ConcurrentHashMap<>();

    private Map<String,Integer> lastIndexVisitMap = new ConcurrentHashMap<>();

    @Override
    public void doSelect(Invocation invocation) {
        URL[] byteWeightArr = randomWeightMap.get(invocation.getServiceName());
        if(byteWeightArr==null){
            List<URL> urls = invocation.getUrls();
            Integer totalWeight = 0;
            for (URL url : urls) {
                Integer weight = Integer.valueOf(url.getParameters().get("weight"));
                totalWeight += weight;
            }
            byteWeightArr = new URL[totalWeight];
            for (URL url : urls) {
                randomOffset(byteWeightArr, Integer.parseInt(url.getParameters().get("weight")),url);
            }
        }
        Integer lastIndex = lastIndexVisitMap.get(invocation.getServiceName());
        if(lastIndex == null){
            lastIndex = 0;
        }
        URL referUrl = byteWeightArr[lastIndex];
        lastIndex++;
        lastIndexVisitMap.put(invocation.getServiceName(),lastIndex);
        invocation.setReferUrl(referUrl);
    }


    private void randomOffset(URL[] randomWeightArr, int currentWeight,URL currentUrl) {
        Set r = new LinkedHashSet(currentWeight);
        Random random = new Random();
        while (r.size() < currentWeight ) {
            int i = random.nextInt(currentWeight);
            if(randomWeightArr[i]==null){
                randomWeightArr[i] = currentUrl;
                r.add(i);
            }
        }
    }

    public static void main(String[] args) {
        byte[] byteWeightArr = new byte[10];
        byteWeightArr[0] = '1';
        System.out.println(byteWeightArr);
        System.out.println(byteWeightArr[1] == 0);
    }
}
