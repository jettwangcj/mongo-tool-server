package cn.org.wangchangjiu.mongo.tool.controller;

import cn.org.wangchangjiu.mongo.tool.service.MongoSQLExecuteService;
import cn.org.wangchangjiu.mongo.tool.vo.request.SendSqlRequest;
import cn.org.wangchangjiu.mongo.tool.vo.result.ExecuteSQLResultVo;
import cn.org.wangchangjiu.mongo.tool.vo.result.ResultResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname MongoQueryController
 * @Description
 * @Date 2022/7/12 14:29
 * @Created by wangchangjiu
 */

@Slf4j
@RestController
@RequestMapping("/sql")
public class MongoExecuteController {

    @Autowired
    private MongoSQLExecuteService mongoSQLExecuteService;

    @ApiOperation(value = "执行SQL语句")
    @PostMapping("/send")
    public ResultResponse<ExecuteSQLResultVo> sendSql(@RequestBody SendSqlRequest request) {
        return ResultResponse.success(mongoSQLExecuteService.executeSql(request.getSql()));
    }


}
