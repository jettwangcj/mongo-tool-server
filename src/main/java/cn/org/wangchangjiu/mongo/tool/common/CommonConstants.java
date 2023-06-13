package cn.org.wangchangjiu.mongo.tool.common;

import cn.org.wangchangjiu.mongo.tool.context.MongoContextHolder;
import cn.org.wangchangjiu.mongo.tool.exception.BizException;

/**
 * @Classname CommonConstants
 * @Description
 * @Date 2023/6/9 10:55
 * @Created by wangchangjiu
 */
public class CommonConstants {

    public static final String TOKEN_PREFIX = "BACKEND_TOKENS_";

    public static final String USER_PREFIX = "BACKEND_USERS_";

    public static void checkUser(String accountId) {
        if(!accountId.equals(MongoContextHolder.getUser())) {
            new BizException("非法操作");
        }
    }
}
