package com.galaxyt.sagittarius.client.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 被 @Value 注解的属性 INFO
 * @author zhouqi
 * @date 2019-07-03 14:40
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-03 14:40     zhouqi          v1.0.0           Created
 *
 */
public class SpringValue{


    private static final Logger logger = LoggerFactory.getLogger(SpringValue.class);


    /**
     * 从占位符中被提取出来的密钥，如 ${com.a} 的密钥为 com.a
     */
    private String key;

    /**
     * 被 @Value 标注的属性的 Field 的引用
     * 需要将其缓存下来，方便后面实现热更新
     */
    private Field field;

    /**
     * 拥有这个属性的对象的引用
     * 此处使用弱引用，不强制引用这个对象
     * 当这个对象被销毁的时候以便很快被虚拟机清理掉这个引用
     * 当这个引用被清理掉之后，表示当前属性不再进行维护
     */
    private WeakReference<Object> beanRef;

    /**
     * 拥有这个属性的对象的 Name
     */
    private String beanName;

    /**
     * 原始占位符，如 ${com.a}
     */
    private String placeholder;

    /**
     * Field 的类型
     */
    private Class<?> targetType;


    /**
     * 构造函数
     * @param key           从占位符中被提取出来的密钥
     * @param placeholder   占位符
     * @param beanName      拥有这个属性的对象的 Name
     * @param bean          拥有这个属性的对象
     * @param field         被 @Value 标注的属性的 Field 的引用
     */
    public SpringValue(String key, String placeholder, String beanName, Object bean, Field field) {

        this.key = key;
        this.placeholder = placeholder;
        this.beanName = beanName;
        this.beanRef = new WeakReference<>(bean);
        this.field = field;
        this.targetType = field.getType();

    }

    /**
     * 修改当前属性的值
     * @param newValue  新的值
     * @throws IllegalAccessException
     */
    public void update(Object newValue){


        logger.debug("请求修改 {}.{}.{} ，newValue : {} ",this.beanName,this.field.getName(),this.placeholder,newValue);

        //从若引用中得到这个属性所在对象的引用
        Object bean = beanRef.get();

        /*
        若拿不到这个引用代表该引用已被虚拟机回收，不需要再进行维护。
         */
        if (bean == null) {

            logger.debug("{} 已被回收，{} 属性不再维护", this.beanName, this.field.getName());

            return;
        }

        /*
        反射修改这个属性的值
         */
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, bean, newValue);

        logger.debug("{}.{}.{} 修改修改成功", this.beanName, this.field.getName(), this.placeholder);

    }


    /**
     * 判断当前属性是否有效
     * @return
     *  依据条件引用该属性的引用是否为null
     *  若为null则无效
     *  若不为null则有效
     *
     */
    boolean isTargetBeanValid() {
        return beanRef.get() != null;
    }


    public Field getField() {
        return field;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getKey() {
        return key;
    }
}
