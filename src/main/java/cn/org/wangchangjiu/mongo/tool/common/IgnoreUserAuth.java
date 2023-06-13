package cn.org.wangchangjiu.mongo.tool.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Classname IgnoreUserAuth
 * @Description
 * @Date 2023/6/13 14:45
 * @Created by wangchangjiu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
public @interface IgnoreUserAuth {
}
