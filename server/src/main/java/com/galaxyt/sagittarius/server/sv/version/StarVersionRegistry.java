package com.galaxyt.sagittarius.server.sv.version;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 全部的最新版本号缓存中心
 * @author zhouqi
 * @date 2019-07-11 11:54
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-11 11:54     zhouqi          v1.0.0           Created
 *
 */
public enum StarVersionRegistry {


    INSTANCE;


    /*
    缓存全部的 StarVersion
    key     :    applicationName.namespace
    value   :    version
     */
    private final Map<String, StarVersion> registry = new ConcurrentHashMap<>();



    /**
     * 添加一个 StarVersion 的监听者
     * @param deferredResultWrapper
     */
    public void addObserver(StarVersionNotificationsListener deferredResultWrapper) {

        String key = this.generateKey(deferredResultWrapper.getApplicationName(), deferredResultWrapper.getNamespace());

        //从缓存中根据 key 拿出现有的 StarVersion
        StarVersion starVersion = registry.get(key);

        /*
        若根据监听者提供的 applicationName 和 namespace 获取不到缓存的 StarVersion
        则证明客户端配置的 applicationName 和 namespace 不正确，需要通知客户端这个错误
        若存在看其本地的版本号是否与当前缓存的版本号是否一致
            一致：添加监听
            不一致：直接通知客户端需要更新

         */
        if (starVersion != null) {  //正常情况
            if (starVersion.getVersion() > deferredResultWrapper.getVersion()) {
                deferredResultWrapper.setResult();
            } else {
                starVersion.addObserver(deferredResultWrapper);
            }
        } else {    //若查找不到缓存
            // 若远程没有客户端需要的缓存则新增一个版本号为 0 的 StarVersion ，为其添加监听
            starVersion = new StarVersion(deferredResultWrapper.getApplicationName(), deferredResultWrapper.getNamespace(), 0);
            starVersion.addObserver(deferredResultWrapper);

        }


    }

    /**
     * 新增一个 StarVersion
     * 内部逻辑会根据组装的 key 先从缓存中获取，若能插到到则修改其 verison，若找不到则直接新增
     * @param starVersion
     */
    public void addStarVersion(StarVersion starVersion) {

        String key = this.generateKey(starVersion.getApplicationName(), starVersion.getNamespace());

        StarVersion oldStarVersion = registry.get(key);

        if (oldStarVersion == null) {
            registry.put(key, starVersion);
        } else {
            oldStarVersion.setVersion(starVersion.getVersion());
        }


    }


    /**
     * 获取服务端的 version
     * @param applicationName
     * @param namespace
     * @return
     */
    public Integer getVersion(String applicationName,String namespace) {

        String key = this.generateKey(applicationName, namespace);

        StarVersion starVersion = registry.get(key);

        if (starVersion == null) {
            return null;
        }

        return starVersion.getVersion();

    }


    /**
     * 缓存 Map key 的生成策略
     * @param applicationName
     * @param namespace
     * @return
     */
    private String generateKey(String applicationName, String namespace) {
        return applicationName + "." + namespace;
    }





}
