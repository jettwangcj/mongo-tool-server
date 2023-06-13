package cn.org.wangchangjiu.mongo.tool.controller;

import cn.org.wangchangjiu.mongo.tool.common.IgnoreUserAuth;
import cn.org.wangchangjiu.mongo.tool.service.AccountService;
import cn.org.wangchangjiu.mongo.tool.vo.request.AccountRequest;
import cn.org.wangchangjiu.mongo.tool.vo.request.LoginRequest;
import cn.org.wangchangjiu.mongo.tool.vo.request.ResetPwdRequest;
import cn.org.wangchangjiu.mongo.tool.vo.request.UpdatePwdRequest;
import cn.org.wangchangjiu.mongo.tool.vo.result.ResultResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     *  用户登录
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @IgnoreUserAuth
    public ResultResponse<String> login(@Valid @RequestBody LoginRequest loginRequest)  {
        return ResultResponse.success(accountService.login(loginRequest.getUserName(), loginRequest.getPassword()));
    }

    @PutMapping("/logout")
    @ApiOperation(value = "退出登录")
    @IgnoreUserAuth
    public ResultResponse<Void> logout() {
        //accountService.logout();
        return ResultResponse.success();
    }


    @GetMapping("/authenticate")
    @ApiOperation(value = "认证token是否有效,并获取登录信息")
    public ResultResponse<String> authenticate(@RequestParam(name = "token") String token) {
        return ResultResponse.success(accountService.authenticate(token));
    }


    @PostMapping("/save")
    @ApiOperation(value = "创建用户")
    @IgnoreUserAuth
    public ResultResponse<Void> saveAccount(@Valid @RequestBody AccountRequest accountRequest) {
        accountService.saveAccount(accountRequest);
        return ResultResponse.success();
    }

    @PostMapping("/updatePwd")
    @ApiOperation(value = "更新密码")
    public ResultResponse<Void> updateAccountPassWord(@Valid @RequestBody UpdatePwdRequest request) {
        accountService.updateAccountPassWord(request.getAccountId(), request.getOldPassWord(), request.getNewPassWord());
        return ResultResponse.success();
    }

    @PostMapping("/resetPwd")
    @ApiOperation(value = "重置密码")
    public ResultResponse<Void> resetAccountPassWord(@Valid @RequestBody ResetPwdRequest request) {
        accountService.resetAccountPassWord(request.getAccountId(), request.getNewPassWord());
        return ResultResponse.success();
    }

}
