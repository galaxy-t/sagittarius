package com.galaxyt.sagittarius.common.dto;

/**
 * 配置 DTO
 * 用于标识一行配置
 * 客户端与服务端传递使用
 * @author zhouqi
 * @date 2019-07-16 09:55
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-16 09:55     zhouqi          v1.0.0           Created
 *
 */
public class StarDto {


    private String starKey;

    private String starValue;

    public String getStarKey() {
        return starKey;
    }

    public void setStarKey(String starKey) {
        this.starKey = starKey;
    }

    public String getStarValue() {
        return starValue;
    }

    public void setStarValue(String starValue) {
        this.starValue = starValue;
    }
}
