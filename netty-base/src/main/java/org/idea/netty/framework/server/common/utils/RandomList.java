package org.idea.netty.framework.server.common.utils;



import java.util.*;

/**
 * @Author linhao
 * @Date created in 12:08 下午 2021/2/3
 */
public class RandomList<T> {

    private List<T> randomList;

    public RandomList(int len) {
        randomList = new ArrayList<>(len);
    }

    public List<T> getRandomList() {
        return randomList;
    }

    public void setRandomList(List<T> randomList) {
        this.randomList = randomList;
    }


    public void randomAdd(T item) {
        if (randomList == null) {
            return;
        }
        if (randomList.size() < 3) {
            randomList.add(item);
            return;
        }
        Random random = new Random();
        int swapSize = random.nextInt(randomList.size() - 1);
        T temp = randomList.get(swapSize);
        randomList.add(temp);
        randomList.set(swapSize, item);
    }

    public void display() {
        System.out.println(randomList.toString());
    }

    public static void main(String[] args) {
        RandomList<Integer> randomList = new RandomList(10);
        randomList.randomAdd(1);
        randomList.randomAdd(2);
        randomList.randomAdd(4);
        randomList.randomAdd(5);
        randomList.randomAdd(6);
        randomList.randomAdd(7);
        randomList.randomAdd(7);
        randomList.randomAdd(7);
        randomList.randomAdd(9);
        randomList.randomAdd(2);
        randomList.randomAdd(1);
        randomList.randomAdd(3);
        randomList.randomAdd(4);
        randomList.randomAdd(5);
        randomList.randomAdd(10);
        randomList.display();
    }

}
