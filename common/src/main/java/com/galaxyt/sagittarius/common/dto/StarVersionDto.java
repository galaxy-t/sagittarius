package com.galaxyt.sagittarius.common.dto;


import java.util.List;

/**
 * 用于标识一个版本的 StarVersion
 * 其中包含版本必要信息和版本的全部配置
 * @author zhouqi
 * @date 2019-07-15 10:51
 * @version v1.0.0
 * @Description 
 * 
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-15 10:51     zhouqi          v1.0.0           Created
 *
 */
public class StarVersionDto {


    private String applicationName;

    private String namespace;

    /**
     * 版本号
     * 无论如何该值会有效
     * 如果服务端未找到匹配的配置该值设置为 0
     */
    private int version;

    /**
     * 配置列表
     * 若不存在配置则该属性为 NULL
     */
    private List<StarDto> stars;

    public StarVersionDto() {
    }

    public StarVersionDto(String applicationName, String namespace, int version) {
        this.applicationName = applicationName;
        this.namespace = namespace;
        this.version = version;
    }

    public StarVersionDto(String applicationName, String namespace, int version, List<StarDto> stars) {
        this.applicationName = applicationName;
        this.namespace = namespace;
        this.version = version;
        this.stars = stars;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<StarDto> getStars() {
        return stars;
    }

    public void setStars(List<StarDto> stars) {
        this.stars = stars;
    }
}
