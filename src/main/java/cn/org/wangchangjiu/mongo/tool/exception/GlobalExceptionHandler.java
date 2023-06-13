package cn.org.wangchangjiu.mongo.tool.exception;

import cn.org.wangchangjiu.mongo.tool.vo.result.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ace on 2017/9/8.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthException.class)
    public ResultResponse clientTokenExceptionHandler(HttpServletResponse response, AuthException ex) {
        response.setStatus(403);
        logger.error(ex.getMessage(),ex);
        return ResultResponse.failure(ex.getERROR_CODE(), ex.getMessage());
    }


    @ExceptionHandler(BizException.class)
    public ResultResponse bizExceptionHandler(HttpServletResponse response, BizException ex) {
        response.setStatus(200);
        logger.error(ex.getMessage(),ex);
        return ResultResponse.failure(ex.getERROR_CODE(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(500);
        logger.error(ex.getMessage(),ex);
        return ResultResponse.failure(null, ex.getMessage());
    }

}
