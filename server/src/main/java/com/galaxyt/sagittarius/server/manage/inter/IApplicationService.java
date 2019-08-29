package com.galaxyt.sagittarius.server.manage.inter;

import com.galaxyt.sagittarius.server.enums.ResultCode;
import com.galaxyt.sagittarius.server.pojo.dto.ApplicationDto;
import com.galaxyt.sagittarius.server.pojo.vo.ApplicationVo;

import java.util.List;

/**
 * 应用业务层
 * @author zhouqi
 * @date 2019-08-20 17:10
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-08-20 17:10     zhouqi          v1.0.0           Created
 *
 */
public interface IApplicationService {


    ResultCode put(ApplicationDto application);


    int remove(int id);

    int edit(ApplicationDto application);

    ApplicationVo detail(int id);

    List<ApplicationVo> list(Integer userId);
}
