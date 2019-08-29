package com.galaxyt.sagittarius.client.spring;

import com.galaxyt.sagittarius.client.SagittariusProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;

/**
 * 注解处理器
 * 用于处理被 @Value 注解标注的属性
 * @author zhouqi
 * @date 2019-07-03 15:22
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-03 15:22     zhouqi          v1.0.0           Created
 *
 */
public class SpringValueProcessor extends SagittariusProcessor implements BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringValueProcessor.class);


    /**
     * Bean 工场
     */
    private BeanFactory beanFactory;

    /**
     * Field 处理
     * 检查这个 Field 是否被 @Value 标注
     * 若标注则对其进行进一步处理
     * @param bean      拥有这个属性的 Bean
     * @param beanName  BeanName 若程序中没有进行指定则默认为类名首字母小写
     * @param field     属性的 Field
     */
    @Override
    protected void processField(Object bean, String beanName, Field field) {

        /*
        检查这个 Field 是否进行 @Value 标注
         */
        Value value = field.getAnnotation(Value.class);
        if (value == null) {
            return;
        }

        SpringValueRepository.INSTANCE.put(bean, beanName, field);

        logger.debug("请求缓存加载 @Value 属性，{} . {}", beanName, field.getName());
    }


    /**
     * 注入 Bean 工场
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
