package com.galaxyt.sagittarius.server.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 应用新增/修改接口参数
 * @author zhouqi
 * @date 2019-08-20 10:23
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-08-20 10:23     zhouqi          v1.0.0           Created
 *
 */
@Getter
@Setter
@ToString
public class ApplicationDto {

    /**
     * 主键ID，若新增时该字段可为NULL
     */
    private Integer id;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名不能为空")
    private String name;



}
