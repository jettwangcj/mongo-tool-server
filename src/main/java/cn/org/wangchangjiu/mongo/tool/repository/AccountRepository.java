package cn.org.wangchangjiu.mongo.tool.repository;

import cn.org.wangchangjiu.mongo.tool.repository.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Classname AccountRepository
 * @Description
 * @Date 2023/6/5 19:40
 * @Created by wangchangjiu
 */
public interface AccountRepository extends MongoRepository<Account,String> {
    Account findByUserName(String userName);
}
