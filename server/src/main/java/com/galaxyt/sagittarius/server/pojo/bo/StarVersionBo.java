package com.galaxyt.sagittarius.server.pojo.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 版本
 * @author zhouqi
 * @date 2019-07-17 17:26
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-17 17:26     zhouqi          v1.0.0           Created
 *
 */
@Getter
@Setter
public class StarVersionBo {


    private String application;

    private String namespace;

    private int version;

}
