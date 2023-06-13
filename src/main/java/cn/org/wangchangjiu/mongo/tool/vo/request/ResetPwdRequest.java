package cn.org.wangchangjiu.mongo.tool.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Classname ResetPwdRequest
 * @Description
 * @Date 2021/9/23 17:46
 * @Created by wangchangjiu
 */
@Data
public class ResetPwdRequest implements Serializable {

    @ApiModelProperty(value = "账户ID")
    @NotNull(message = "账户ID不能为空")
    private String accountId;

    @ApiModelProperty(value = "重置的新密码")
    @NotNull(message = "新密码不能为空")
    private String newPassWord;

}
