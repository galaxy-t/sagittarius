package com.galaxyt.sagittarius.client;

import com.galaxyt.sagittarius.client.config.SagittariusProperties;
import com.galaxyt.sagittarius.client.spring.SpringValueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启动加载自定义配置
 * 用于注册一些 Bean
 * @author zhouqi
 * @date 2019-07-04 17:16
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-04 17:16     zhouqi          v1.0.0           Created
 *
 */
public class SagittariusRegistrar implements ImportBeanDefinitionRegistrar {


    private static final Logger logger = LoggerFactory.getLogger(SagittariusRegistrar.class);

    /**
     * 注册自定义的 Bean
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        /*
        若不启用 Sagittarius Client ，直接返回
         */
        if (!SagittariusProperties.isEnable()) {
            return;
        }


        /*
        向 Spring 注册自定义的 @Value 注解处理器
         */
        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(SpringValueProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(SpringValueProcessor.class.getName(), beanDefinition);


    }
}
