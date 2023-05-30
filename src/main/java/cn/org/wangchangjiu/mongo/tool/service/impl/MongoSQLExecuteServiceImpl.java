package cn.org.wangchangjiu.mongo.tool.service.impl;

import cn.org.wangchangjiu.mongo.tool.vo.result.ExecuteSQLResultVo;
import cn.org.wangchangjiu.mongo.tool.vo.result.NotQueryExecuteSQLResultVo;
import cn.org.wangchangjiu.mongo.tool.vo.result.QueryExecuteSQLResultVo;
import cn.org.wangchangjiu.sqltomongo.core.exception.SqlParserException;
import cn.org.wangchangjiu.sqltomongo.core.parser.data.PartSQLParserData;
import cn.org.wangchangjiu.sqltomongo.core.util.SqlCommonUtil;
import cn.org.wangchangjiu.mongo.tool.service.MongoSQLExecuteService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sqltomongo.SQLToMongoTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname MongoSQLExecuteServiceImpl
 * @Description
 * @Date 2022/7/15 16:59
 * @Created by wangchangjiu
 */
@Slf4j
@Service
public class MongoSQLExecuteServiceImpl implements MongoSQLExecuteService {

    @Autowired
    private SQLToMongoTemplate sqlToMongoTemplate;

    @Override
    public ExecuteSQLResultVo executeSql(String sql) {

        SqlCommonUtil.SqlType sqlType;
        try {
            sqlType = SqlCommonUtil.getSqlType(sql);
        } catch (JSQLParserException exception) {
            throw new SqlParserException("解析失败：" + exception);
        }

        if (sqlType == SqlCommonUtil.SqlType.SELECT) {
            // 查询
            return selectResult(sql);
        } else {
            return execute(sql);
        }
    }

    private NotQueryExecuteSQLResultVo execute(String sql) {
        return null;
    }

    private QueryExecuteSQLResultVo selectResult(String sql) {
        List<String> columns;
        List<Map> data;
        try {
            data = sqlToMongoTemplate.selectList(sql, Map.class);
            // 解析 投影字段
            PartSQLParserData parserData = sqlToMongoTemplate.sqlParserData(sql);
            columns = parserData.getProjectData().stream().map(item -> StringUtils.isNotEmpty(item.getAlias()) ? item.getAlias() : item.getField()).collect(Collectors.toList());
            data = data.stream().map(item -> {
                if(item.containsKey("_id") && item.get("_id") != null){
                    Object id = item.get("_id");
                    item.put("_id", id.toString());
                }
                return item;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("执行SQL查询失败：{}", ex);
            return QueryExecuteSQLResultVo.fail(ex.getMessage());
        }
        return QueryExecuteSQLResultVo.success(columns, data);
    }


}
