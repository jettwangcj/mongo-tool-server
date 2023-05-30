package cn.org.wangchangjiu.mongo.tool.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname MongoDatabaseRepository
 * @Description
 * @Date 2022/7/8 14:00
 * @Created by wangchangjiu
 */
@Component
public class MongoDatabaseRepository {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    private  MongoDatabaseFactory localDatabaseFactory = null;

    private static final Map<String, MongoDatabaseFactory> MONGO_CLIENT_DATABASE_FACTORY_CACHE = new ConcurrentHashMap<>();

    public MongoDatabaseFactory getLocal() {
        if (StringUtils.isEmpty(connectionString)) {
            throw new RuntimeException("连接Mongo信息未加载。。。");
        }
        MongoClient mongoClient;
        String database;
        try {
            mongoClient = MongoClients.create(connectionString);
            database = new ConnectionString(connectionString).getDatabase();
        } catch (Exception ex) {
            throw new RuntimeException("Mongo 连接错误：" + ex);
        }
        localDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongoClient, database);
        return localDatabaseFactory;
    }

    public MongoDatabaseFactory get(String databaseKey) {
        if(StringUtils.isEmpty(databaseKey)){
            // 默认配置文件的
            return this.localDatabaseFactory;
        }
        return MONGO_CLIENT_DATABASE_FACTORY_CACHE.get(databaseKey);
    }


    /**
     *  异步创建 Mongo DatabaseFactory
     * @param connectionId
     * @param url
     * @param dataBaseName
     */
    @Async
    public synchronized void asyncCreateMongoDataBase(String connectionId, String url, String dataBaseName) {
        String key = connectionId.concat("#").concat(dataBaseName);
        if(MONGO_CLIENT_DATABASE_FACTORY_CACHE.containsKey(connectionId) || MONGO_CLIENT_DATABASE_FACTORY_CACHE.containsKey(key)){
            return;
        }
        MongoClient mongoClient;
        String database = dataBaseName;
        try {
            mongoClient = MongoClients.create(url);
            if(StringUtils.isEmpty(database)){
                database = new ConnectionString(url).getDatabase();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Mongo 连接错误：" + ex);
        }
        MONGO_CLIENT_DATABASE_FACTORY_CACHE.put(connectionId.concat("#").concat(database), new SimpleMongoClientDatabaseFactory(mongoClient, database));
    }
}
