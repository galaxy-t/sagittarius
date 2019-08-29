package com.galaxyt.sagittarius.server;

import com.galaxyt.sagittarius.server.manage.dao.StarVersionDao;
import com.galaxyt.sagittarius.server.pojo.bo.StarVersionBo;
import com.galaxyt.sagittarius.server.sv.version.StarVersion;
import com.galaxyt.sagittarius.server.sv.version.StarVersionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 应用初始化操作
 * @author zhouqi
 * @date 2019-07-17 17:17
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 17:17     zhouqi          v1.0.0           Created
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)  //让其尽可能的早
public class SagittariusApplicationContextInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SagittariusApplicationContextInitializer.class);


    @Autowired
    private StarVersionDao starVersionDao;

    /**
     * 将全部的最新的版本号缓存到内存中去
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        logger.info("Sagittarius server init start...");

        List<StarVersionBo> starVersionBos = this.starVersionDao.selectNewestStarVersion();


        if (starVersionBos != null && starVersionBos.size() > 0) {

            for (StarVersionBo starVersionBo : starVersionBos) {
                StarVersion starVersion = new StarVersion(starVersionBo.getApplication(), starVersionBo.getNamespace(), starVersionBo.getVersion());

                StarVersionRegistry.INSTANCE.addStarVersion(starVersion);

                logger.info("application:{},namespace:{},version:{}", starVersion.getApplicationName(), starVersion.getNamespace(), starVersion.getVersion());

            }

        }

        logger.info("Sagittarius server init end...");
    }


}
