package org.idea.netty.framework.server.rpc.provider;

import lombok.SneakyThrows;
import org.idea.netty.framework.server.rpc.RpcData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Author linhao
 * @Date created in 3:38 下午 2021/2/10
 */
public class ProviderHandler implements ProviderFactory {

    private ProviderQueue<RpcData> providerQueue;

    private final Lock lock = new ReentrantLock();

    private Condition notFull = lock.newCondition();

    private Condition notEmpty = lock.newCondition();

    public ProviderHandler(int size) {
        this.providerQueue = new ProviderQueue(size);
    }

    /**
     * 接收数据之后放入消费队列里面
     *
     * @param rpcData
     */
    @Override
    public void receive(RpcData rpcData) {
        lock.lock();
        try {
            while (providerQueue.isFull()) {
                System.err.println("消费队列已满,SIZE IS :" + providerQueue.getSize());
                notFull.await();
            }
            providerQueue.push(rpcData);
            System.out.println("接收任务：" + rpcData);
            notEmpty.signalAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将队列中的数据逐个进行处理
     */
    @Override
    public void receiveHandler() {
        lock.lock();
        try {
            while (providerQueue.isEmpty()) {
                notEmpty.await();
            }
            RpcData rpcData = providerQueue.offer();
            System.out.println("处理任务：" + rpcData);
            notFull.signalAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ProviderFactory providerFactory = new ProviderHandler(10);
        Thread consumerThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    providerFactory.receive(new RpcData(null, i));
                }
            }
        });

        Thread providerThread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    providerFactory.receiveHandler();
                }
                System.out.println("全部处理完毕");
            }
        });

        consumerThread.start();
        providerThread.start();
        Thread.yield();
        System.out.println("======");
    }

}
