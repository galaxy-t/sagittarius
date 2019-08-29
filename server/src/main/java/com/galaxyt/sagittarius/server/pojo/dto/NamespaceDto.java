package com.galaxyt.sagittarius.server.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author zhouqi
 * @date 2019-07-18 10:11
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-18 10:11     zhouqi          v1.0.0           Created
 *
 */
@Getter
@Setter
public class NamespaceDto {


    private Integer id;

    @NotBlank(message = "Namespace name 不能为空")
    private String name;

    private Integer applicationId;


    


}
