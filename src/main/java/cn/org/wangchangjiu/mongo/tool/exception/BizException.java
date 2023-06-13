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
public class BizException extends RuntimeException {

    /**
     * D0400 用户请求参数错误 二级宏观错误码
     */
    private String ERROR_CODE = "B0001";


    public BizException(String message) {
        super(message);
    }


}
