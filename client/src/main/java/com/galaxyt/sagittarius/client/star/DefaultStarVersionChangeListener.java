package com.galaxyt.sagittarius.client.star;

import com.galaxyt.sagittarius.client.spring.SpringValue;
import com.galaxyt.sagittarius.client.spring.SpringValueRepository;
import com.galaxyt.sagittarius.common.dto.StarDto;

/**
 * 非首次初始化拉取
 * @author zhouqi
 * @date 2019-07-17 14:34
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 14:34     zhouqi          v1.0.0           Created
 *
 */
public class DefaultStarVersionChangeListener extends AbstractStarVersionChangeListener {


    @Override
    public void onChange(StarDto star) {

        SpringValue springValue = SpringValueRepository.INSTANCE.get(star.getStarKey());
        springValue.update(star.getStarValue());

    }
}
