package com.galaxyt.sagittarius.client.config;

/**
 * 配置加载顺序
 * @author zhouqi
 * @date 2019-07-15 14:47
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-15 14:47     zhouqi          v1.0.0           Created
 *
 */
public enum ConfigOrdered {

    /**
     * 本地优先
     * 默认配置
     * 若设置为本地优先，Sagittarius 在进行配置反射时会先去读取配置文件中是否存在该配置的 key ，若存在不论本地配置是否为空，都以本地为准
     */
    LOCAL_FIRST,

    /**
     * 远程优先
     * 若配置远程优先，Sagittarius 在进行配置反射时则不会考虑本地配置，只要远程存在即全部覆盖，若远程不存在而本地存在，则以本地为准
     */
    REMOTE_FIRST;

}
