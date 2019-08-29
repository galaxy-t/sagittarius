package com.galaxyt.sagittarius.server.sv.web;

import com.galaxyt.sagittarius.server.sv.version.StarVersionNotificationsListener;
import com.galaxyt.sagittarius.server.sv.version.StarVersionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Star 版本变动通知
 * @author zhouqi
 * @date 2019-07-11 14:41
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-11 14:41     zhouqi          v1.0.0           Created
 *
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {



    //TODO 完成长链接服务端
    //TODO 完成长链接的维持，将其缓存起来，以方便多个长链接仅返回一次就可以
    //TODO 失败情况处理
    //TODO 超时情况处理
    //TODO 完成情况处理

    /**
     * 长链接通知接口
     * @param applicationName
     * @param namespace
     * @param version
     * @param clientIp
     * @param clientPort
     * @return
     */
    @GetMapping
    public DeferredResult<ResponseEntity> pollNotification(@RequestParam String applicationName,
                                                           @RequestParam String namespace,
                                                           @RequestParam Integer version,
                                                           @RequestParam(required = false) String clientIp,
                                                           @RequestParam(required = false) String clientPort) {

        /**
         * 新建一个版本号通知监听器
         */
        StarVersionNotificationsListener notificationsListener =
                new StarVersionNotificationsListener(applicationName, namespace, version);
        /**
         * 将这个监听器添加到被监听者上
         */
        StarVersionRegistry.INSTANCE.addObserver(notificationsListener);

        //返回
        return notificationsListener.getResult();

    }









}
