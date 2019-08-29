package com.galaxyt.sagittarius.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 全局配置属性
 * 需要在配置中心客户端加载的第一步预先加载这些属性
 * @author zhouqi
 * @date 2019-07-17 10:05
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 10:05     zhouqi          v1.0.0           Created
 *
 */
public class SagittariusProperties {


    private static final Logger logger = LoggerFactory.getLogger(SagittariusProperties.class);

    /**
     * 是否已经进行过初始化
     */
    private static final AtomicBoolean isInitialize = new AtomicBoolean(false);

    /**
     * 应用名称
     */
    private static String applicationName;

    /**
     * 命名空间
     */
    private static String namespace;

    /**
     * 默认使用顺序，本地 OR 远程
     */
    private static ConfigOrdered order;

    /**
     * 服务端 URL
     */
    private static String serverUrl;

    /**
     * 默认扫描的包路径
     */
    private static String scanBasePackages;

    /**
     * 是否启用
     * 默认为 false
     * 该属性永远有效
     */
    private static boolean enable = false;

    /**
     * 版本检查 URL
     */
    private static String starVersionNotificationsUrl;

    /**
     * 拉取最新配置 URL
     */
    private static String starPullUrl;

    private static Environment environment;


    /**
     * 初始化参数
     * 互斥锁
     * 保证仅初始化一次
     *
     * @param enable
     * @param applicationName
     * @param namespace
     * @param order
     * @param scanBasePackages
     * @param serverUrl
     */
    public synchronized static void init(boolean enable, String applicationName, String namespace, ConfigOrdered order, String scanBasePackages, String serverUrl, Environment environment) {

        if (!isInitialize.compareAndSet(false, true)) {
            logger.error("Sagittarius 配置参数已被初始化,已拒绝被再次进行初始化。");
            return;
        }

        SagittariusProperties.enable = enable;
        SagittariusProperties.applicationName = applicationName;
        SagittariusProperties.namespace = namespace;
        SagittariusProperties.order = order;
        SagittariusProperties.scanBasePackages = scanBasePackages;
        SagittariusProperties.serverUrl = serverUrl;

        SagittariusProperties.environment = environment;
        initUrl();

    }


    private static void initUrl() {

        String prefix = "http://" + SagittariusProperties.serverUrl + "/";

        String suffix = "?applicationName=" + applicationName + "&namespace=" + namespace + "&version=";

        SagittariusProperties.starVersionNotificationsUrl = prefix + "notifications" + suffix;
        SagittariusProperties.starPullUrl = prefix + "pull" + suffix;


    }


    public static String getApplicationName() {
        return applicationName;
    }

    public static String getNamespace() {
        return namespace;
    }

    public static ConfigOrdered getOrder() {
        return order;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static String getScanBasePackages() {
        return scanBasePackages;
    }

    public static boolean isEnable() {
        return enable;
    }

    public static String getStarVersionNotificationsUrl() {
        return starVersionNotificationsUrl;
    }

    public static String getStarPullUrl() {
        return starPullUrl;
    }

    public static Environment getEnvironment() {
        return environment;
    }
}
