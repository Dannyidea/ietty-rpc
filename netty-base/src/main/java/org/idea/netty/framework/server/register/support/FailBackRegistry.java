package org.idea.netty.framework.server.register.support;

import org.idea.netty.framework.server.common.TimeWheel;
import org.idea.netty.framework.server.common.URL;
import org.idea.netty.framework.server.register.Register;


/**
 * @Author linhao
 * @Date created in 12:23 下午 2021/1/6
 */
public abstract class FailBackRegistry extends AbstractRegistry implements Register {

    private int times = 0;

    public FailBackRegistry(URL url) {
        super(url);
    }

    @Override
    public void register(URL url) {
        super.register(url);
        try {
            //todo
            this.doRegister(url);
        } catch (Exception e) {
            e.printStackTrace();
            //添加重试任务,容错设计
            addFailedRegisteredTask(url);
        }
    }

    private class FailBackRetryTask implements Runnable {

        private FailBackRegistry failBackRegistry;

        private URL url;

        private FailBackRetryTask(FailBackRegistry failBackRegistry, URL url) {
            this.failBackRegistry = failBackRegistry;
            this.url = url;
        }

        @Override
        public void run() {
            System.out.println("执行注册url");
            failBackRegistry.doRegister(url);
        }
    }

    private void addFailedRegisteredTask(URL url) {
        TimeWheel timeWheel = TimeWheel.buildDefaultTimeWheel();
        timeWheel.add(new FailBackRetryTask(this, url), "failBackRetryTask", times);
        Thread workerThread = new Thread(new TimeWheel.Worker(timeWheel));
        workerThread.setDaemon(true);
        workerThread.start();
    }

    @Override
    public void unRegister(URL url) {

    }



    /**
     * 留给子类进行实现所用
     */
    public abstract void doRegister(URL url);


}
