package com.galaxyt.sagittarius.server.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_namespace")
public class Namespace {



    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField(value = "name",strategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(value = "application_id")
    private int applicationId;


}
