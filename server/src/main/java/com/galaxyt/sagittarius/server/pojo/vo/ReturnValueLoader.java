package com.galaxyt.sagittarius.server.pojo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.galaxyt.sagittarius.server.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回值装载器
 * @author zhouqi
 * @date 2019-01-13 23:22
 * @version v1.0.0
 * @Description
 *  全部的API接口的返回信息必须返回成本类型的对象进行返回
 *  若没有返回信息也要返回本类型对象，data默认为null即可
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-01-13 23:22     zhouqi          v1.0.0           Created
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReturnValueLoader {


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 要返回的数据
     * 注：若data为null，data节点将不会出现在json中
     */
    private Object data;


    public ReturnValueLoader() {
        super();
    }

    /**
     *
     * @param code  状态码
     * @param msg   状态信息
     */
    public ReturnValueLoader(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     *
     * @param code  状态码
     * @param msg   状态信息
     * @param data  要返回的数据
     */
    public ReturnValueLoader(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    /**
     *
     * @param resultCode
     */
    public ReturnValueLoader(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public ReturnValueLoader(Object object) {

        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
        this.data = object;

    }

}
