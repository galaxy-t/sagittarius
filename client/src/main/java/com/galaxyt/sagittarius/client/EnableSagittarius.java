package com.galaxyt.sagittarius.client;

import com.galaxyt.sagittarius.client.config.ConfigOrdered;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 射手座配置中心客户端启动注释
 * @author zhouqi
 * @date 2019-07-03 15:29
 * @version v1.0.0
 * @Description
 * 本注解请仅声明一次，多次声明以全局扫描到的第一个为准，且不保证每次的扫描顺序
 * 本注解的声明位置与 scanBasePackages 参数无关
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-03 15:29     zhouqi          v1.0.0           Created
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SagittariusRegistrar.class)
public @interface EnableSagittarius {

    /**
     * 应用名称
     * 服务端不会认为不存在的 applicationName 为错误，若服务端不存在这个 applicationname ，那么它认为这个 applicationName 是一个初始化状态
     * @return
     */
    String applicationName();

    /**
     * 加载命名空间
     * 服务端不会认为不存在的 namespace 为错误，若服务端不存在这个 namespace ，那么它认为这个 namespace 是一个初始化状态
     * @return
     */
    String namespace();

    /**
     * Sagittarius 配置的顺序，默认为 Integer.MAX_VALUE。
     * 如果在不同的 Sagittarius 配置中存在具有相同名称的属性，则具有较小订单的 Sagittarius 配置将获胜。
     * @return
     */
    ConfigOrdered order() default ConfigOrdered.LOCAL_FIRST;

    /**
     * 服务端配置地址
     * 也可以将其配置到配置文件中
     * 若此处进行配置，则以此处为准
     * 格式应该为 192.168.1.2:8080
     * @return
     */
    String serverUrl() default "";

    /**
     * 扫描包路径
     * 限定扫描哪些包路径下的 @Value 注解，客户端仅在限定目录下生效
     * 若不设定则默认为全部路径
     * @return
     */
    String scanBasePackages() default "";

    /**
     * 是否启用客户端
     * 默认为 true
     * 即当添加本注解时默认生效
     * @return
     */
    boolean enable() default true;

}
