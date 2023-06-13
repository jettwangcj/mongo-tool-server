package cn.org.wangchangjiu.mongo.tool.repository.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 *  后台账号表
 *
 */
@Data
@Document(value = "Account")
public class Account implements Serializable {

    @Id
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 密码加密用到的盐
     */
    @ApiModelProperty("密码加密用到的盐")
    private String salt;

}
