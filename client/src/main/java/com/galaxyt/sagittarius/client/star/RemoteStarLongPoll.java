package com.galaxyt.sagittarius.client.star;

import com.galaxyt.sagittarius.client.config.SagittariusProperties;
import com.galaxyt.sagittarius.client.utils.SagittariusThreadFactory;
import com.galaxyt.sagittarius.client.utils.http.HttpLongPollUtil;
import com.galaxyt.sagittarius.client.utils.http.Response;
import com.galaxyt.sagittarius.common.dto.StarVersionDto;
import com.galaxyt.sagittarius.common.exception.SagittariusException;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 远程长轮询
 *
 * @author zhouqi
 * @version v1.0.0
 * @date 2019-07-05 16:06
 * @Description Modification History:
 * Date                 Author          Version          Description
 * ---------------------------------------------------------------------------------*
 * 2019-07-05 16:06     zhouqi          v1.0.0           Created
 */
public class RemoteStarLongPoll {


    private static final Logger logger = LoggerFactory.getLogger(RemoteStarLongPoll.class);


    /**
     * 用于标识是否已经开始轮询
     */
    private final AtomicBoolean longPollStarted;

    /**
     * 用于标识是否需要轮询停止
     */
    private final AtomicBoolean longPollStopped;

    /**
     * 线程池
     * 用于执行长轮询
     */
    private final ExecutorService longPollExecutorService;

    /**
     * 初始轮询等待时间
     */
    private final long longPollingInitialDelayInMills;


    /**
     * 令牌桶
     * 用于限流轮询发起次数，最大每秒两次
     */
    private final RateLimiter longPollRateLimiter;

    /**
     * 长链接 工具类
     */
    private final HttpLongPollUtil httpLongPollUtil;


    /**
     * 系统当前版本号，默认为0
     * 将会去服务端获取最新的版本号
     */
    private final AtomicInteger currentStarVersion;

    private final Gson gson;

    /**
     * 是否为系统首次初始化加载
     */
    private final AtomicBoolean isInitPull;


    public RemoteStarLongPoll() {

        //初始化默认轮询未开启
        this.longPollStarted = new AtomicBoolean(false);
        //初始化默认轮询启动之后不停止
        this.longPollStopped = new AtomicBoolean(false);

        //创建轮询线程池，使用守护线程来进行轮询
        this.longPollExecutorService = Executors.newSingleThreadExecutor(
                SagittariusThreadFactory.create("RemoteStarLongPollService", true));

        //设置令牌桶每秒限流两个
        this.longPollRateLimiter = RateLimiter.create(2);

        //长轮询工具类
        this.httpLongPollUtil = new HttpLongPollUtil();

        //设置初始版本为 0
        this.currentStarVersion = new AtomicInteger(0);


        this.longPollingInitialDelayInMills = 2000;

        this.gson = new Gson();

        this.isInitPull = new AtomicBoolean(true);
    }

    /**
     * 启动长轮询
     */
    public void star() {

        /*
        若轮询已经开始则不再进行启动
         */
        if (!this.longPollStarted.compareAndSet(false, true)) {
            logger.debug("长轮训已经被启动，将不再进行重复启动");
            //已经启动
            return;
        }

        try {
            this.longPollExecutorService.submit(() -> {

                /*
                若设置了初始化等待时间则进行等待
                 */
                if (longPollingInitialDelayInMills > 0) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(longPollingInitialDelayInMills);
                    } catch (InterruptedException e) {
                        //忽略
                    }
                }

                //刷新一次轮询
                refresh();


            });
        } catch (Throwable throwable) {
            this.longPollStarted.set(false);
        }

    }

    /**
     * 刷新轮询
     */
    public void refresh() {

        /*
        只要轮询处于未启动且没有通知说需要关闭当前线程则判定为启动轮询
         */
        while (!longPollStopped.get() && !Thread.currentThread().isInterrupted()) {

            /*
            考虑到万一代码中存在不可预直的 bug ，且这些 bug 被利用到来针对服务端发起攻击的情况，如 DDos 攻击
            此处使用令牌桶进行限流，且如果短时间内并发两次则必须进行等待
            正常情况下令牌桶限流不会起到实质性的作用
             */
            if (!longPollRateLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
                //若没有拿到令牌则等待五秒之后再发起轮询请求
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    //忽略
                }
            }


            String url = SagittariusProperties.getStarVersionNotificationsUrl() + currentStarVersion.get();

            try {
                logger.debug("准备发起长轮询，URL:{}", url);

                //响应码要么是 200 要么是 304，其它情况均会抛出异常
                Response response = httpLongPollUtil.doGet(url);

                /*
                若响应码为 200 则代表有新版本返回，其它情况均忽略
                 */
                if (response.getCode() == 200) {
                    logger.debug("发现新版本，准备执行拉取操作");
                    pullStar();
                    logger.debug("新版本拉取结束");
                }

            } catch (Throwable ex) {
                logger.error("长轮询失败，等待两秒继续请求", ex);

                try {
                    //出现异常等待两秒
                    TimeUnit.SECONDS.sleep(longPollingInitialDelayInMills);
                } catch (InterruptedException ie) {
                    //忽略
                }
            } finally {
                logger.debug("长轮询结束");
            }
        }

    }

    /**
     * 拉取远程配置
     * 无论如何都会拉取服务端的最新配置
     */
    public void pullStar() {

        String url = SagittariusProperties.getStarPullUrl();

        try {
            //只要请求成功，response 的 code 无论如何都为 200
            Response response = httpLongPollUtil.doGet(url);

            /*
            为避免出现 BUG 还是先进行一遍验证
             */
            if (response.getCode() == 200 && !StringUtils.isEmpty(response.getBody())) {

                StarVersionDto starVersionDto = gson.fromJson(response.getBody(), StarVersionDto.class);

                /*
                若存在配置信息
                 */
                if (starVersionDto.getStars() != null && starVersionDto.getStars().size() > 0) {

                    StarVersionChangeListener starVersionChangeListener = null;

                    if (isInitPull.compareAndSet(true,false)) {    //若当前版本号为 0 则视为初始化加载
                        starVersionChangeListener = new InitPullStar();
                    } else {    //不为 0 则视为后续改动
                        starVersionChangeListener = new DefaultStarVersionChangeListener();
                    }

                    starVersionChangeListener.onChange(starVersionDto);

                }

                //设置当前版本号为最新的版本号
                //只有在有修改配置内容的情况下才更新当前版本号
                //考虑这样做是否正确，若服务端因为某些原因存入了一个空的版本
                //在还没有添加有变动的版本之前是否要一直尝试拉取新的版本
                //或许认为空的版本也是一个版本即可
                currentStarVersion.set(starVersionDto.getVersion());

                logger.debug("更新配置完成，当前本地配置版本为:{}", starVersionDto.getVersion());

            }

        } catch (Throwable ex) {

            // TODO 此处考虑在拉取出错的情况下是否要将当前版本号设置为 0 ，以便下次接着拉取

            logger.error("拉取最新配置失败");
            throw new SagittariusException("拉取最新配置失败", ex);
        }

    }





}
