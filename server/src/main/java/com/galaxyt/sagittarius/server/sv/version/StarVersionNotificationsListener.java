package com.galaxyt.sagittarius.server.sv.version;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Observable;
import java.util.Observer;

/**
 * 返回结果集包装器
 * 同时也是 StarVersion 的观察者
 * 每一个 StarVersion 都带有 applicationName 和 namespace
 * 观察者自身也会带有这两个属性 以观察者的这两个属性与现有的 StarVersion 中的这两个属性进行匹配
 * @author zhouqi
 * @date 2019-07-11 10:54
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-11 10:54     zhouqi          v1.0.0           Created
 *
 */
public class StarVersionNotificationsListener implements Observer {

    /**
     * 设置超时时间，60秒
     */
    private static final long TIMEOUT = 60 * 1000;

    /**
     * 未有修改动作的响应
     * 默认的返回响应，超时等其它情况直接返回 304
     * 绝大部分情况下，长轮训都是返回 304 超时，所以将其缓存下来，不用每次都创建一次，以避免浪费资源
     */
    private static final ResponseEntity NOT_MODIFIED_RESPONSE_LIST = new ResponseEntity(HttpStatus.NOT_MODIFIED);

    /**
     * 返回结果集
     */
    private DeferredResult<ResponseEntity> result;

    /**
     * 本次请求的应用名称
     */
    private String applicationName;

    /**
     * 本次请求的命名空间
     */
    private String namespace;

    /**
     * 客户端当前版本号
     */
    private Integer version;

    /**
     * 初始化基础信息
     * @param applicationName
     * @param namespace
     * @param version
     */
    public StarVersionNotificationsListener(String applicationName, String namespace, Integer version) {

        this.applicationName = applicationName;
        this.namespace = namespace;
        this.version = version;

        /*
        初始化返回结果集
        默认为 60 秒超时
        默认为返回 403
         */
        result = new DeferredResult<>(TIMEOUT, NOT_MODIFIED_RESPONSE_LIST);
    }

    /**
     * 返回结果集
     * @return
     */
    public DeferredResult<ResponseEntity> getResult() {
        return result;
    }

    /**
     * 对 StarVersion 变动的监听
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

        StarVersion starVersion = (StarVersion) o;

        int newVersion = starVersion.getVersion();

        /*
        当进入此方法内时候证明该 StarVersion 已经发生过变动，即调用其 setVersion 方法
        正常情况此时新的版本号一定比旧的版本号要大
        但是可以会存在一些其它的情况，如数据库出现脏读等
        所以加一个判断，仅当新版本号大于旧版本号时通知客户端
        否则继续监听
        要求管理后台逻辑每次改动的版本号必须递增
         */
        if (newVersion > version) {
            o.deleteObserver(this);
            this.setResult();
        }
    }


    /**
     * 设置结果集内容
     * 当客户端接收到的 code 为 200 时，证明其 applicationName 和 namespace 已经发生改变，需要进行更新操作
     */
    public void setResult() {
        result.setResult(new ResponseEntity(HttpStatus.OK));

    }

    /**
     * 超时处理
     * @param timeoutCallback
     */
    public void onTimeout(Runnable timeoutCallback) {
        result.onTimeout(timeoutCallback);
    }

    /**
     * 完成处理
     * @param completionCallback
     */
    public void onCompletion(Runnable completionCallback) {
        result.onCompletion(completionCallback);
    }



    public String getApplicationName() {
        return applicationName;
    }

    public String getNamespace() {
        return namespace;
    }

    public Integer getVersion() {
        return version;
    }


}
