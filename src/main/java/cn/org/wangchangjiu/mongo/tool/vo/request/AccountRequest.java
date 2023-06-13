package cn.org.wangchangjiu.mongo.tool.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname AccountRequest
 * @Description
 * @Date 2021/9/23 17:37
 * @Created by wangchangjiu
 */
@Data
public class AccountRequest implements Serializable {

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

}

