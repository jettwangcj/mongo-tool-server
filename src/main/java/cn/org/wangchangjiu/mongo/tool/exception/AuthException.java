package cn.org.wangchangjiu.mongo.tool.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @Classname OrderException
 * @Description
 * @Date 2022/8/2 17:34
 * @Created by wangchangjiu
 */
@Getter
@ToString
public class AuthException extends RuntimeException {

    /**
     * D0400 用户请求参数错误 二级宏观错误码
     */
   private String ERROR_CODE = "A0001";

    private static final String DEFAULT_ERROR_MESSAGE = "用户未登录";



    public AuthException() {
        super(DEFAULT_ERROR_MESSAGE);
    }


}
