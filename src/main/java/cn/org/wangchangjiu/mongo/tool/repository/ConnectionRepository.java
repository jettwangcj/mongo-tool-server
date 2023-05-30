package cn.org.wangchangjiu.mongo.tool.repository;

import cn.org.wangchangjiu.mongo.tool.repository.entity.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Classname ConnectionRepository
 * @Description
 * @Date 2022/7/12 13:52
 * @Created by wangchangjiu
 */
public interface ConnectionRepository extends MongoRepository<Connection,String> {
}
