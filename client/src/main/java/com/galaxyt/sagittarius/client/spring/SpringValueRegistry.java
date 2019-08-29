package com.galaxyt.sagittarius.client.spring;

import com.galaxyt.sagittarius.client.utils.SagittariusThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 被 @Value 注解标注的属性注册器
 * 用于缓存记录这些属性
 * 其中包含定时清理这些属性的任务
 * @author zhouqi
 * @date 2019-07-04 11:28
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-04 11:28     zhouqi          v1.0.0           Created
 *
 */
public class SpringValueRegistry {


    private final Logger logger = LoggerFactory.getLogger(SpringValueRegistry.class);


    /**
     * 属性清理时间间隔
     * 单位：秒
     */
    private final long CLEAN_INTERVAL_IN_SECONDS = 5;

    /**
     * TODO
     * 此处使用 ConcurrentHashMap 并不能够保证全部的配置信息一起被更新，需要对整个的缓存进行加锁
     * ConcurrentHashMap 使用的是分段锁，它仅为更新操作加锁，而且保证最小粒度，jdk8 貌似粒度小到键值对
     * 但是若存在以下情况：
     *      配置中有非对称加密的公钥和私钥，若不能保证两个密钥同时被更新，可能会存在延迟上的误差导致某些程序运行失败
     * 在更新操作的时候或许需要加上互斥锁
     * 在执行更新之前必须先清楚的知道哪些值需要被更新，以保证更新时间最短
     */
    private final Map<String, SpringValue> registry = new ConcurrentHashMap<>(16, 0.75f, 4);

    /**
     * 是否已经进行了初始化，默认 false
     */
    private final AtomicBoolean initialized = new AtomicBoolean(false);



    public void register(String key, SpringValue springValue) {

        registry.put(key, springValue);

        // 懒加载
        if (initialized.compareAndSet(false, true)) {
            initialize();
        }
    }


    /**
     * 根据 key 获取 SpringValue
     * @param key
     * @return
     */
    public SpringValue get(String key) {
        return registry.get(key);
    }




    /**
     * 初始化方法
     */
    private void initialize() {

        /*
        定时任务
        任务创建 CLEAN_INTERVAL_IN_SECONDS 秒后，每个 CLEAN_INTERVAL_IN_SECONDS 进行一次属性扫描清理
         */
        Executors.newSingleThreadScheduledExecutor(SagittariusThreadFactory.create("SpringValueRegistry",true)).scheduleAtFixedRate(
            () -> {
                scanAndClean();
            },CLEAN_INTERVAL_IN_SECONDS, CLEAN_INTERVAL_IN_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 清理不需要维护的属性
     * 清理条件为该属性引用不存在
     */
    private void scanAndClean() {

        logger.info("SpringValue 定时清理启动");

        Iterator<SpringValue> iterator = registry.values().iterator();

        while (!Thread.currentThread().isInterrupted() && iterator.hasNext()) {
            SpringValue springValue = iterator.next();
            if (!springValue.isTargetBeanValid()) {
                // 删除这个不被使用的属性
                iterator.remove();

                logger.debug("{}.{}.{}不再需要被维护，执行删除。",springValue.getBeanName(),springValue.getField().getName(),springValue.getPlaceholder());

            }
        }

        logger.info("SpringValue 定时清理完成");
    }
}

