package cn.org.wangchangjiu.mongo.tool.service.impl;

import cn.org.wangchangjiu.mongo.tool.common.CommonConstants;
import cn.org.wangchangjiu.mongo.tool.context.MongoContextHolder;
import cn.org.wangchangjiu.mongo.tool.vo.result.DataBaseTreeVo;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import cn.org.wangchangjiu.mongo.tool.repository.ConnectionRepository;
import cn.org.wangchangjiu.mongo.tool.repository.MongoDatabaseRepository;
import cn.org.wangchangjiu.mongo.tool.repository.entity.Connection;
import cn.org.wangchangjiu.mongo.tool.service.MongoConnectionService;
import cn.org.wangchangjiu.mongo.tool.vo.request.SaveConnectionRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname MongoConnectionServiceImpl
 * @Description
 * @Date 2022/7/8 17:50
 * @Created by wangchangjiu
 */
@Service
public class MongoConnectionServiceImpl implements MongoConnectionService {

    @Autowired
    private ConnectionRepository repository;

    @Autowired
    private MongoDatabaseRepository databaseRepository;

    @Override
    public void createConnection(SaveConnectionRequest request) {
        ConnectionString connectionString = new ConnectionString(request.getUrl());
        List<String> hosts = connectionString.getHosts();
        Connection connection;
        if(StringUtils.isNotEmpty(request.getId())){
            connection = repository.findById(request.getId()).orElseThrow(() -> new RuntimeException("资源不存在"));
        } else {
            connection = new Connection();
        }
        connection.setName(request.getName());
        connection.setUrl(request.getUrl());
        connection.setHosts(String.join(",", hosts));
        connection.setAccountId(MongoContextHolder.getUser());
        repository.save(connection);
    }


    private List<DataBaseTreeVo> queryConnectionList() {
        List<Connection> connections = repository.findByAccountId(MongoContextHolder.getUser());
        return CollectionUtils.isEmpty(connections) ? new ArrayList<>() : connections.stream().map(item -> new DataBaseTreeVo(item.getId(), item.getName(), item.getUrl(), false)).collect(Collectors.toList());
    }

    @Override
    public void deleteConnection(String id) {
        Connection connection = repository.findById(id).orElseThrow(() -> new RuntimeException("资源不存在"));
        CommonConstants.checkUser(connection.getAccountId());
        repository.delete(connection);
    }

    private List<DataBaseTreeVo> queryDataBases(String connectionId) {
        Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
        CommonConstants.checkUser(connection.getAccountId());
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        List<DataBaseTreeVo> databases = new ArrayList<>();
        mongoClient.listDatabaseNames().forEach(item -> databases.add(new DataBaseTreeVo(connection.getId(), item, connection.getUrl(), false)));
        mongoClient.close();
        return databases;
    }

    @Override
    public List<DataBaseTreeVo> queryDataBaseTrees(Integer level, String connectionId, String dataBaseName) {
        if(level == 0){
            // 获取连接
            return this.queryConnectionList();
        } else if(level == 1){
            // 获取数据库
            if(connectionId == null){
                throw new RuntimeException("连接参数不能为空");
            }
            return this.queryDataBases(connectionId);
        } else if (level == 2){
            if(connectionId == null || StringUtils.isEmpty(dataBaseName)){
                throw new RuntimeException("连接参数或者数据库不能为空");
            }

            Connection connection = repository.findById(connectionId).orElseThrow(() -> new RuntimeException("资源不存在"));
            CommonConstants.checkUser(connection.getAccountId());
            databaseRepository.asyncCreateMongoDataBase(connection.getId(), connection.getUrl(), dataBaseName);
            // 获取文档
            return this.queryDocument(connection, dataBaseName);
        }
        return null;
    }

    private List<DataBaseTreeVo> queryDocument(Connection connection, String dataBaseName) {
        MongoClient mongoClient = MongoClients.create(connection.getUrl());
        MongoDatabase database = mongoClient.getDatabase(dataBaseName);
        List<DataBaseTreeVo> documents = new ArrayList<>();
        database.listCollectionNames().forEach(item -> documents.add(new DataBaseTreeVo(connection.getId(), item, connection.getUrl(), true)));
        mongoClient.close();
        return documents;
    }


}
