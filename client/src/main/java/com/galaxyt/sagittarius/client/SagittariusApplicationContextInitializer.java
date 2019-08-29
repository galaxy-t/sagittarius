package com.galaxyt.sagittarius.client;

import com.galaxyt.sagittarius.client.config.SagittariusProperties;
import com.galaxyt.sagittarius.client.star.RemoteStarLongPoll;
import com.galaxyt.sagittarius.common.exception.SagittariusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 初始化程序
 * @author zhouqi
 * @date 2019-07-04 17:18
 * @version v1.0.0
 * @Description
 * 初始化程序仅尝试一次，不做多次尝试，初始化失败立即给予提示
 * 使用者需要每次启动自行检查日志
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-04 17:18     zhouqi          v1.0.0           Created
 *
 */
public class SagittariusApplicationContextInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger logger = LoggerFactory.getLogger(SagittariusApplicationContextInitializer.class);

    /**
     * 是否已经进行初始化，默认为 false
     */
    private static final AtomicBoolean isInitialize = new AtomicBoolean(false);

    private ConfigurableApplicationContext applicationContext;

    /**
     * 配置中心客户端初始化操作
     * @param applicationContext
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        /*
        如果已经进行初始化则不再进行初始化操作
         */
        if (!isInitialize.compareAndSet(false, true)) {
            return;
        }

        this.applicationContext = applicationContext;

        boolean bool = sacnEnableSagittarius();

        /*
        初始化成功，且启用客户端
         */
        if (bool && SagittariusProperties.isEnable()) {
            logger.info("客户端开始发起长链接，进行首次加载配置信息。");

            RemoteStarLongPoll remoteStarLongPoll = new RemoteStarLongPoll();

            remoteStarLongPoll.pullStar();

            remoteStarLongPoll.star();

        }


    }

    /**
     * 扫描全部的 Class 得到 EnableSagittarius 注解
     * @return
     *  若带有 EnableSagittarius 注解则返回 true
     *  若没有则返回 false
     */
    private boolean sacnEnableSagittarius() {


        //获取全部的堆栈节点
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();


        for (StackTraceElement stackTraceElement : stackTrace) {    //循环堆栈节点找到 带有 main 函数的那个类

            //加载 Class
            Class clazz = null;
            try {
                clazz = Class.forName(stackTraceElement.getClassName());
            } catch (ClassNotFoundException e) {
                //或许会存在某些异常 BUG ，客户端并不关心这些问题
                //若该类出现异常则抛弃掉该类，继续循环
                //直到全部循环结束
                e.printStackTrace();
                continue;
            }

            //检查是否带有 启动注解
            EnableSagittarius enableSagittarius = (EnableSagittarius) clazz.getAnnotation(EnableSagittarius.class);

            /*
            若找到 EnableSagittarius 注解
            初始化参数
            返回 true 结束本方法
             */
            if (enableSagittarius != null) {
                return initProperties(enableSagittarius);
            }
        }


        return false;
    }

    /**
     * 初始化配置信息
     * @param enableSagittarius
     * @return
     */
    private boolean initProperties(EnableSagittarius enableSagittarius) {

        /*
        若客户端设置为禁用
        那么当次初始化也为成功
        仅仅是不需要初始化
         */
        if (!enableSagittarius.enable()) {
            return true;
        }

        String serverUrl = enableSagittarius.serverUrl();

        //若注解中包含 serverUrl 则使用注解中的
        //若注解中不包含则去配置文件中取
        serverUrl = StringUtils.isEmpty(serverUrl) ? applicationContext.getEnvironment().getProperty("sagittarius.serverUrl") : serverUrl;

        if (StringUtils.isEmpty(serverUrl)) {
            logger.error("Sagittarius 初始化配置失败，请检查 serverurl");
            throw new SagittariusException("Sagittarius 初始化配置失败，请检查 serverurl");
        }

        //初始化配置信息
        SagittariusProperties.init(enableSagittarius.enable(),
                enableSagittarius.applicationName(),
                enableSagittarius.namespace(),
                enableSagittarius.order(),
                enableSagittarius.scanBasePackages(),
                serverUrl, applicationContext.getEnvironment());

        logger.info("Sagittarius 客户端初始配置信息成功, enable:{},applicationName:{},namespace:{},order:{},scanBasePackages:{},serverUrl:{}",
                enableSagittarius.enable(),
                enableSagittarius.applicationName(),
                enableSagittarius.namespace(),
                enableSagittarius.order(),
                enableSagittarius.scanBasePackages(),
                enableSagittarius.serverUrl());

        return true;
    }


}
