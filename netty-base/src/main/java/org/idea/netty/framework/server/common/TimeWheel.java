package org.idea.netty.framework.server.common;

/**
 * 时间轮
 *
 * @Author idea
 * @Date created in 5:08 下午 2020/12/1
 *
 * 使用方式：
 *  TimeWheel timeWheel = TimeWheel.buildDefaultTimeWheel();
 *  //3秒延迟，实现心跳策略
 *  timeWheel.add(new HeartBeatTask(),"heartBeatTask",3);
 *
 */
public class TimeWheel {

    private int size;
    private long index;
    private TaskNode head;
    /**
     * 槽位数组
     */
    private TaskNode[] nodeArr;

    /**
     * 容量
     */
    private int capacity;

    /**
     * 默认时间轮槽数目
     */
    private final static int DEFAULT_CAPACITY = 10;

    public TimeWheel(int capacity) {
        nodeArr = new TaskNode[capacity];
        this.capacity = capacity;
        for (int i = 0; i < capacity; i++) {
            TaskNode taskNode = new TaskNode(i);
            this.addOne(taskNode);
        }
    }

    /**
     * 构建默认大小的时间轮
     *
     * @return
     */
    public static TimeWheel buildDefaultTimeWheel() {
        //初始化定义 时间轮一圈为10秒
        return new TimeWheel(DEFAULT_CAPACITY);
    }

    /**
     * 对外提供的增加任务入口
     *
     * @param task     任务
     * @param taskName 任务名称
     * @param delay    延迟时长
     */
    public void add(Runnable task, String taskName, long delay) {
        long cycles = delay / 10;
        int nodeArrIndex = (int) (delay % 10);
        Coordinates coordinates = new Coordinates(-1, nodeArrIndex, cycles);
        Task taskItem = new Task(task, taskName, coordinates);
        TaskNodeQueue taskNodeQueue = nodeArr[nodeArrIndex].taskNodeQueue;
        if (taskNodeQueue == null) {
            taskNodeQueue = new TaskNodeQueue();
        }
        taskNodeQueue.push(taskItem);
        nodeArr[nodeArrIndex].taskNodeQueue = taskNodeQueue;
    }

    /**
     * 内部构造函数调用
     *
     * @param taskNode
     */
    private void addOne(TaskNode taskNode) {
        if (size >= capacity) {
            throw new IllegalArgumentException("error taskNode,taskNode's size is over");
        }
        nodeArr[size] = taskNode;
        size++;
    }

    public void display() {
        System.out.println(nodeArr);
    }


    public static void main(String[] args) {
        TimeWheel timeWheel = TimeWheel.buildDefaultTimeWheel();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行任务");
            }
        });
        for (int i = 0; i < 100; i++) {
            if(i%5==0){
                timeWheel.add(thread, "test-task-" + i, i);
            }
        }

        Thread workerThread = new Thread(new Worker(timeWheel));
        workerThread.setDaemon(true);
        workerThread.start();
        while (true) {

        }
    }

    /**
     * 任务节点队列
     */
    private static class TaskNodeQueue {

        private Task head;
        private long size;

        public TaskNodeQueue() {
        }

        public boolean push(Task taskNode) {
            if (head == null) {
                head = taskNode;
                size++;
                return true;
            }
            for (Task temp = head; temp != null; ) {
                if (temp.next == null) {
                    temp.next = taskNode;
                    size++;
                    return true;
                }
                temp = temp.next;
            }
            return false;
        }

        public void display() {
            Task temp = head;
            while (temp != null) {
                temp = temp.next;
            }
        }

    }

    /**
     * 任务
     */
    private static class Task {
        private Runnable job;

        private Task next;

        private String jobName;

        /**
         * 坐标
         */
        private Coordinates coordinates;

        public Task(Runnable job, String jobName, Coordinates coordinates) {
            this.job = job;
            this.jobName = jobName;
            this.coordinates = coordinates;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public Runnable getJob() {
            return job;
        }

        public void setJob(Runnable job) {
            this.job = job;
        }
    }

    /**
     * 任务坐标
     */
    private class Coordinates {

        /**
         * 队列下标
         */
        private long queueIndex;

        /**
         * 槽位索引
         */
        private int taskNodeIndex;

        /**
         * 圈数目
         */
        private long cycles;

        public long getQueueIndex() {
            return queueIndex;
        }

        public void setQueueIndex(long queueIndex) {
            this.queueIndex = queueIndex;
        }

        public int getTaskNodeIndex() {
            return taskNodeIndex;
        }

        public void setTaskNodeIndex(int taskNodeIndex) {
            this.taskNodeIndex = taskNodeIndex;
        }

        public long getCycles() {
            return cycles;
        }

        public void setCycles(long cycles) {
            this.cycles = cycles;
        }

        public Coordinates(long queueIndex, int taskNodeIndex, long cycles) {
            this.queueIndex = queueIndex;
            this.taskNodeIndex = taskNodeIndex;
            this.cycles = cycles;
        }

        @Override
        public String toString() {
            return "Coordinates{" +
                    "queueIndex=" + queueIndex +
                    ", taskNodeIndex=" + taskNodeIndex +
                    ", cycles=" + cycles +
                    '}';
        }
    }

    /**
     * 任务槽点
     */
    private static class TaskNode {

        private TaskNodeQueue taskNodeQueue;

        private int state;

        private long index;


        private static final short NEW = 0;
        private static final short COMPLETING = 1;
        private static final short NORMAL = 2;
        private static final short EXCEPTIONAL = 3;
        private static final short CANCELLED = 4;
        private static final short INTERRUPTING = 5;
        private static final short INTERRUPTED = 6;

        public TaskNode(long index) {
            this.index = index;
            this.state = NEW;
        }

        public TaskNodeQueue getTaskNodeQueue() {
            return taskNodeQueue;
        }

        public void pushTask(Task task) {
            if (this.taskNodeQueue == null) {
                this.taskNodeQueue = new TaskNodeQueue();
            }
            taskNodeQueue.push(task);
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setTaskNodeQueue(TaskNodeQueue taskNodeQueue) {
            this.taskNodeQueue = taskNodeQueue;
        }


    }


    /**
     * 工作线程
     */
    public static class Worker implements Runnable {

        private TimeWheel timeWheel;

        private long currentCycle;

        private long beginArrIndex;

        private long endArrIndex;

        public Worker(TimeWheel timeWheel) {
            this.timeWheel = timeWheel;
        }

        @Override
        public void run() {
            //循环便利时间轮里面的每个槽点位置
            int i = 0;
            currentCycle = 0;
            while (true) {
                TaskNode taskNode = timeWheel.nodeArr[i];
                TaskNodeQueue taskNodeQueue = taskNode.getTaskNodeQueue();
                if(taskNodeQueue!=null){
                    Task item = taskNodeQueue.head;
                    while (item != null) {
                        Coordinates coordinates = item.getCoordinates();
                        //当前圈数
                        if (coordinates.getCycles() == this.currentCycle) {
                            //执行任务
                            new Thread(item.getJob()).start();
                            System.out.println(item.jobName);
                        }
                        item = item.next;
                    }
                }
                try {
                    if (i < timeWheel.size - 1) {
                        i++;
                    } else {
                        i = 0;
                        currentCycle++;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            for (TaskNode taskNode : timeWheel.nodeArr) {
//                TaskNodeQueue taskNodeQueue = taskNode.getTaskNodeQueue();
//                Task task = taskNodeQueue.head;
//                Task nextTask = task;
//                while(nextTask!=null){
//                    nextTask.getCoordinates();
//                    nextTask = nextTask.next;
//                }
//            }
        }
    }

}
