package com.galaxyt.sagittarius.server.sv.version;

import java.util.Observable;

/**
 * 修改版本号
 * 被观察者
 * 用来记录每一个应用所对应的每一个命名空间的当前版本
 * @author zhouqi
 * @date 2019-07-11 13:54
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-11 13:54     zhouqi          v1.0.0           Created
 *
 */
public class StarVersion extends Observable  {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 命名空间  dev 等等
     */
    private String namespace;

    /**
     * 当前版本号
     */
    private int version;


    /**
     * 初始化
     * 本对象中的三个属性必须使用构造函数进行初始化
     * @param applicationName   应用名称
     * @param namespace         命名空间
     * @param version           版本号
     */
    public StarVersion(String applicationName, String namespace, int version) {
        this.applicationName = applicationName;
        this.namespace = namespace;
        this.version = version;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getNamespace() {
        return namespace;
    }

    public int getVersion() {
        return version;
    }

    /**
     * 要求全部的修改必须使用本方法
     * 以达到通知监听者的功能
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
        super.setChanged();
        super.notifyObservers();
    }


    @Override
    public String toString() {
        return "StarVersion{" +
                "applicationName='" + applicationName + '\'' +
                ", namespace='" + namespace + '\'' +
                ", version=" + version +
                '}';
    }
}
