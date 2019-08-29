package com.galaxyt.sagittarius.client.star;

import com.galaxyt.sagittarius.client.config.ConfigOrdered;
import com.galaxyt.sagittarius.client.config.SagittariusProperties;
import com.galaxyt.sagittarius.common.dto.StarDto;
import com.galaxyt.sagittarius.common.dto.StarVersionDto;

/**
 *
 * @author zhouqi
 * @date 2019-07-17 14:47
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 14:47     zhouqi          v1.0.0           Created
 *
 */
public abstract class AbstractStarVersionChangeListener implements StarVersionChangeListener {


    public abstract void onChange(StarDto str);

    @Override
    public void onChange(StarVersionDto starVersion) {

        for (StarDto star : starVersion.getStars()) {

            ConfigOrdered order = SagittariusProperties.getOrder();

            /*
            若本地优先且本地配置中存在这个 key 那么忽略这个配置
             */
            if (order == ConfigOrdered.LOCAL_FIRST && SagittariusProperties.getEnvironment().containsProperty(star.getStarKey())) {
                continue;
            }

            onChange(star);
        }

    }


}
