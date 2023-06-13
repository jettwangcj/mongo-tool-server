package cn.org.wangchangjiu.mongo.tool.service;

import cn.org.wangchangjiu.mongo.tool.vo.request.AccountRequest;

public interface AccountService {
    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    String login(String userName, String password) ;

    /**
     * 退出登录
     * @param accountId
     */
    void logout(String accountId);

    /**
     * 保存账号
     * @param accountRequest
     */
    void saveAccount(AccountRequest accountRequest);



    /**
     * 修改密码
     * @param accountId
     * @param oldPassWord
     * @param newPassWord
     */
    void updateAccountPassWord(String accountId,String oldPassWord,String newPassWord);

    /**
     * 重只密码
     * @param accountId
     * @param newPassWord
     */
    void resetAccountPassWord(String accountId,String newPassWord);

    /**
     *  验证token
     * @param token
     * @return
     */
    String authenticate(String token);
}
