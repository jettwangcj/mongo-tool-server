package cn.org.wangchangjiu.mongo.tool.interceptor;

import cn.org.wangchangjiu.mongo.tool.common.IgnoreUserAuth;
import cn.org.wangchangjiu.mongo.tool.context.MongoContextHolder;
import cn.org.wangchangjiu.mongo.tool.exception.AuthException;
import cn.org.wangchangjiu.mongo.tool.repository.MongoDatabaseRepository;
import cn.org.wangchangjiu.mongo.tool.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname MongoDBInterceptor
 * @Description
 * @Date 2022/7/8 11:06
 * @Created by wangchangjiu
 */
@Component
public class MongoInterceptor implements HandlerInterceptor {

    @Autowired
    private MongoDatabaseRepository repository;

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = null;
        if (HandlerMethod.class.isInstance(handler)) {
            handlerMethod = HandlerMethod.class.cast(handler);
        } else if (ResourceHttpRequestHandler.class.isInstance(handler)) {
            return true;
        }

        // 配置该注解，说明不进行用户拦截
        IgnoreUserAuth annotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserAuth.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(IgnoreUserAuth.class);
        }
        if (annotation != null) {
            return true;
        }


        String auth = request.getHeader("auth");
        if(StringUtils.isEmpty(auth)){
            throw new AuthException();
        }
        String accountId = accountService.authenticate(auth);
        if(StringUtils.isEmpty(accountId)){
            throw new AuthException();
        }
        MongoContextHolder.setUser(accountId);

        String token = request.getHeader("token");
        MongoDatabaseFactory mongoDatabaseFactory = repository.get(token);
        MongoContextHolder.setMongoDatabaseFactory(mongoDatabaseFactory);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex){
        MongoContextHolder.remove();
    }

}
