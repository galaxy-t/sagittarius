package com.galaxyt.sagittarius.common.exception;

/**
 * Sagittarius 全部自定义异常基类，可以直接使用，也可以继承再使用
 * @author zhouqi
 * @date 2019-07-16 09:57
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-16 09:57     zhouqi          v1.0.0           Created
 *
 */
public class SagittariusException extends RuntimeException {


    public SagittariusException(String msg) {
        super(msg);
    }

    public SagittariusException(Throwable cause) {
        super(cause);
    }

    public SagittariusException(String message, Throwable cause) {
        super(message, cause);
    }
}
