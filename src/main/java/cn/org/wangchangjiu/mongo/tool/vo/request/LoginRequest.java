package cn.org.wangchangjiu.mongo.tool.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Classname LoginRequest
 * @Description
 * @Date 2021/9/23 17:24
 * @Created by wangchangjiu
 */
@Data
public class LoginRequest implements Serializable {

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

}
