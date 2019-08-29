package com.galaxyt.sagittarius.client.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Thread 工场
 * @author zhouqi
 * @date 2019-07-04 10:28
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-04 10:28     zhouqi          v1.0.0           Created
 *
 */
public class SagittariusThreadFactory implements ThreadFactory {

    /**
     * 用于记录整个线程组的线程数量
     * 每增加一个线程都会加1
     * 每个线程 name 的后缀均为该值的当前值
     * 如，第一个线程创建时 threadNumber = 1 ，则该线程 name 为 sagittarius_***_1 ，然后 threadNumber = 2
     */
    private final AtomicLong threadNumber = new AtomicLong(1);

    /**
     * Thread Name 前缀
     */
    private final String namePrefix;

    /**
     * 是否为守护线程
     */
    private final boolean daemon;

    /**
     * 定义线程组
     */
    private static final ThreadGroup threadGroup = new ThreadGroup("sagittarius");

    /**
     * 构造函数
     * @param namePrefix
     * @param daemon
     */
    private SagittariusThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    /**
     * 工场创建方法
     * @param namePrefix
     * @param daemon
     * @return
     */
    public static ThreadFactory create(String namePrefix, boolean daemon) {
        return new SagittariusThreadFactory(namePrefix, daemon);
    }


    /**
     * 等待全部线程进行关机
     * @param timeoutInMillis   将要等待多少毫秒
     *                          该方法在这个时间段内会一直持续检查线程状态的工作
     *                          两次检查需要等待两秒，所以该参数酌情考虑大小
     * @return
     */
    public static boolean waitAllShutdown(int timeoutInMillis) {

        //获取整个射手座线程组
        ThreadGroup group = getThreadGroup();

        //创建一个 Thread 数组，大小为当前活动线程数量
        Thread[] activeThreads = new Thread[group.activeCount()];

        //从线程组中将全部的活动线程放入数组中
        group.enumerate(activeThreads);

        //将数组包装成一个Set
        //当前活动状态的线程集合
        Set<Thread> alives = new HashSet<>(Arrays.asList(activeThreads));

        //用来存放已关机的线程的集合
        Set<Thread> dies = new HashSet<>();
        //log.info("Current ACTIVE thread count is: {}", alives.size());

        //设置操作时间
        //在这些毫秒时间之内会不断尝试检查全部的线程状态
        long expire = System.currentTimeMillis() + timeoutInMillis;

        /*
        只要没到结束时间，当前循环会一直进行，并尝试检查当前全部活动的线程状态
         */
        while (System.currentTimeMillis() < expire) {

            /*
             先对已有线程进行分组
             */
            classify(alives, dies, new ClassifyStandard<Thread>() {

                /**
                 * 检查策略
                 * 若当前线程为非活动状态，则查看该线程是否被外部指定要求优雅关机，若没有则返回false
                 * 若当前线程为活动状态，则查看当前线程是否为守护线程，若不是则返回false
                 * 以上两种情况若返回 true 则判定为关机状态
                 * @param thread
                 * @return
                 */
                @Override
                public boolean satisfy(Thread thread) {
                    return !thread.isAlive() || thread.isInterrupted() || thread.isDaemon();
                }
            });

            if (alives.size() > 0) {    //若依旧有活动的线程等待两秒继续循环
                try {
                    //等待两秒
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    // ignore
                }
            } else {    //若没有活动的线程直接返回 true ，表示当前活动线程已全部关机
                return true;
            }
        }

        //若循环无法在规定的时间内返回 true 则代表依旧有未停止的用户线程
        return false;
    }

    /**
     * 分组策略
     * @param <T>
     */
    private static interface ClassifyStandard<T> {
        boolean satisfy(T thread);
    }

    /**
     * 线程分组
     * @param src       原线程集合
     * @param des       分组之后的线程集合
     * @param standard  分组策略
     * @param <T>
     *
     * @desc 循环原线程集合并以策略检查并分组之后，原线程集合中符合规则的线程会被删除
     */
    private static <T> void classify(Set<T> src, Set<T> des, ClassifyStandard<T> standard) {
        Set<T> set = new HashSet<>();
        for (T t : src) {
            if (standard.satisfy(t)) {
                set.add(t);
            }
        }
        src.removeAll(set);
        des.addAll(set);
    }








    public static ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        //创建一个线程
        //设置线程组
        //设置线程 name 格式为 线程组name_线程name_创建顺序
        Thread thread = new Thread(threadGroup, runnable,threadGroup.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement());
        //设置是否为守护线程
        thread.setDaemon(daemon);
        /*
        设置线程的优先级
        均为正常状态
         */
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
