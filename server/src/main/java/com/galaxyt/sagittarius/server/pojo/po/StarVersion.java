package com.galaxyt.sagittarius.server.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("t_star_version")
public class StarVersion {


    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField(value = "namespace_id")
    private int namespaceId;

}
