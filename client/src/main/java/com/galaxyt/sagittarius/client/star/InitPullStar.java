package com.galaxyt.sagittarius.client.star;

import com.galaxyt.sagittarius.common.dto.StarDto;

/**
 * 首次加载
 * 可能会如下情况
 *  假如有一个：@Value("${a}")
 *  a 这个配置在本地配置文件中不存在，但在服务端存在
 *  那么首次加载应该把这个配置加载出来
 *  所以要将 a 及它的值放到环境变量中以便 spring 将其注入到属性中
 *
 * @author zhouqi
 * @date 2019-07-17 11:46
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 11:46     zhouqi          v1.0.0           Created
 *
 */
public class InitPullStar extends AbstractStarVersionChangeListener {


    @Override
    public void onChange(StarDto star) {
        System.setProperty(star.getStarKey(), star.getStarValue());
    }
}
