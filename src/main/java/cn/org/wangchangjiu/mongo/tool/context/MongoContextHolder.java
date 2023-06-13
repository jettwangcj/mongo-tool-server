package cn.org.wangchangjiu.mongo.tool.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.data.mongodb.MongoDatabaseFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname MongoContext
 * @Description
 * @Date 2022/7/8 11:15
 * @Created by wangchangjiu
 */
public class MongoContextHolder {

    private static final String MONGO_DATABASE_FACTORY_KEY = "mongo_database_factory_key";

    private static final String USER_ID_KEY = "user_id_key";

    private static final ThreadLocal<Map<String, Object>> threadLocal = new TransmittableThreadLocal<>();

    private static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    private static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map.get(key);
    }


    public static void setMongoDatabaseFactory(MongoDatabaseFactory factory) {
        set(MONGO_DATABASE_FACTORY_KEY, factory);
    }

    public static MongoDatabaseFactory getMongoDatabaseFactory(){
        return (MongoDatabaseFactory) get(MONGO_DATABASE_FACTORY_KEY);
    }


    public static void setUser(String userId) {
        set(USER_ID_KEY, userId);
    }

    public static String getUser(){
        return get(USER_ID_KEY) == null ? null : get(USER_ID_KEY).toString();
    }

    public static void remove(){
        threadLocal.remove();
    }

}
