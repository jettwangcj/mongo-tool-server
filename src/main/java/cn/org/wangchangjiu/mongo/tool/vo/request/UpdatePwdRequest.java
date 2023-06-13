package cn.org.wangchangjiu.mongo.tool.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname UpdatePwd
 * @Description
 * @Date 2021/9/23 18:05
 * @Created by wangchangjiu
 */
@Data
public class UpdatePwdRequest implements Serializable {

    @ApiModelProperty(value = "账户ID")
    private String accountId;

    @ApiModelProperty(value = "老密码")
    private String oldPassWord;

    @ApiModelProperty(value = "新密码")
    private String newPassWord;

}
