package com.galaxyt.sagittarius.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Bean 处理器抽象类
 * 用于在Bean初始化前后进行操作
 * @author zhouqi
 * @date 2019-07-03 14:59
 * @version v1.0.0
 * @Description
 *
 *      BeanPostProcessor
 *          提供在 Bean 被初始化前后进行处理的钩子
 *
 *      PriorityOrdered
 *          提供设置 Bean 被 Spring 加载的顺序
 *
 *
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-03 14:59     zhouqi          v1.0.0           Created
 *
 */
public abstract class SagittariusProcessor implements BeanPostProcessor, PriorityOrdered {

    private static final Logger logger = LoggerFactory.getLogger(SagittariusProcessor.class);


    /**
     * 每一个 Bean 初始化之前进行处理
     * 该状态具体应该为该 Bean 已经被 Spring 加载，但未进行初始化
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Class clazz = bean.getClass();

        /*
        将 Bean 中的每一个 Field 进行循环处理
         */
        for (Field field : findAllField(clazz)) {
            processField(bean, beanName, field);
        }


        return bean;
    }

    /**
     * Field 处理
     * @param bean      拥有这个属性的 Bean
     * @param beanName  BeanName 若程序中没有进行指定则默认为类名首字母小写
     * @param field     属性的 Field
     */
    protected abstract void processField(Object bean, String beanName, Field field);

    @Override
    public int getOrder() {
        //让这个 Bean 尽可能晚的被加载
        return Ordered.LOWEST_PRECEDENCE;
    }


    /**
     * 得到这个类的全部的 Field
     * @param clazz
     * @return
     */
    private List<Field> findAllField(Class clazz) {

        final List<Field> res = new LinkedList<>();

        /*
        找到并且循环全部的Field
        使用 Spring 的反射工具类
        其内部进行过迭代优化，效率比较高
         */
        ReflectionUtils.doWithFields(clazz,field -> {
            res.add(field);
        });

        return res;

    }


}
