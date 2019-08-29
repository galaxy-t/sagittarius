package com.galaxyt.sagittarius.server.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_star")
public class Star {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField(value = "star_version_id")
    private int starVersionId;

    @TableField(value = "star_key",strategy = FieldStrategy.NOT_EMPTY)
    private String starKey;

    @TableField(value = "star_value",strategy = FieldStrategy.NOT_EMPTY)
    private String starValue;



}
