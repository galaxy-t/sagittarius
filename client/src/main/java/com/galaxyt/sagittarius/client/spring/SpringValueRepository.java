package com.galaxyt.sagittarius.client.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;

/**
 * 用于操作被 @Value 注解标注的存储
 * @author zhouqi
 * @date 2019-07-05 15:29
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-05 15:29     zhouqi          v1.0.0           Created
 *
 */
public enum SpringValueRepository {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(SpringValueRepository.class);


    private SpringValueRegistry registry = new SpringValueRegistry();


    /**
     * 新增一个 SpringValue
     * @param bean
     * @param beanName
     * @param field
     */
    public void put(Object bean, String beanName, Field field) {


        Value value = field.getAnnotation(Value.class);

        /*
        检查这个占位符中的 key
        仅支持 ${abcd.cb} 格式
         */
        String key = PlaceholderHelper.INSTANCE.extractPlaceholderKey(value.value());
        if (key == null) {
            return;
        }

        /*
        将属性的必要信息进行封装，并进行缓存
        key：占位符中的key，注意该属性应具有唯一性，配置中心服务端在添加时候也应注意检查该项（在每一种模式下属于唯一）
         */
        SpringValue springValue = new SpringValue(key, value.value(), beanName, bean, field);
        registry.register(key, springValue);

        logger.debug("请求加载 @Value 属性完成，{} . {} . {}", beanName, field.getName(),key);


    }

    /**
     * 根据 key 获取 SpringValue
     * @param key
     * @return
     */
    public SpringValue get(String key) {
        return registry.get(key);
    }

}
