package cn.org.wangchangjiu.mongo.tool.service.impl;

import cn.org.wangchangjiu.mongo.tool.common.CommonConstants;
import cn.org.wangchangjiu.mongo.tool.exception.BizException;
import cn.org.wangchangjiu.mongo.tool.repository.AccountRepository;
import cn.org.wangchangjiu.mongo.tool.repository.entity.Account;
import cn.org.wangchangjiu.mongo.tool.service.AccountService;
import cn.org.wangchangjiu.mongo.tool.vo.request.AccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public String login(String userName, String password) {

        Account account = accountRepository.findByUserName(userName);
        if (account == null) {
            log.error("账户：{} 不存在", userName);
            throw new RuntimeException();
        }
        if (!account.getPassword().equals(DigestUtils.md5Hex(password + account.getSalt()))) {
            log.error("账户：{} 输入的密码：{} 不正确", userName, password);
            throw new RuntimeException();
        }
        //删除可能原存在登录数据，减少redis内存占用
      //  logout(account.getId());

        // 账户ID 写入 redis
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(CommonConstants.TOKEN_PREFIX + token, account.getId(), 3, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(CommonConstants.USER_PREFIX + account.getId(), token, 3, TimeUnit.DAYS);
        return token;
    }

    /**
     * 退出登录,清除redis缓存
     *
     * @param accountId
     */
    @Override
    public void logout(String accountId) {
        String redisUserKey = CommonConstants.USER_PREFIX + accountId;
        Object redisTokenValue = redisTemplate.opsForValue().get(redisUserKey);
        if (redisTokenValue != null) {
            log.info("账户：{} 登出", accountId);
            redisTemplate.delete(Arrays.asList(redisUserKey, CommonConstants.TOKEN_PREFIX + redisTokenValue));
        }
    }


    /**
     * 认证token是否有效,并获取登录信息
     *
     * @param token
     * @return
     */
    @Override
    public String authenticate(String token) {
        Object accountId = redisTemplate.opsForValue().get(CommonConstants.TOKEN_PREFIX + token);
        log.info("认证token：{}，redis Key:{}, 获取账户：{}", token, CommonConstants.TOKEN_PREFIX + token, accountId);
        if (accountId == null) {
            return null;
        }
        return accountId.toString();
    }


    /**
     * 创建用户
     *
     * @param accountRequest
     */
    @Override
    @Transactional
    public void saveAccount(AccountRequest accountRequest) {
        Account account = new Account();
        account.setUserName(accountRequest.getUserName());
        long count = accountRepository.count(Example.of(account));
        if (count > 0) {
            log.error("用户名:{} 已存在", accountRequest.getUserName());
           throw new BizException("用户名:" +accountRequest.getUserName()+ " 已存在");
        }
        String salt = UUID.randomUUID().toString();
        String password = DigestUtils.md5Hex(accountRequest.getPassword() + salt);
        account.setPassword(password);
        account.setSalt(salt);
        account.setId(UUID.randomUUID().toString());
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void updateAccountPassWord(String accountId, String oldPassWord, String newPassWord) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("账号信息不存在"));
        if (!account.getPassword().equals(DigestUtils.md5Hex(oldPassWord + account.getSalt()))) {
            throw new BizException("原始密码错误");
        }
        String password = DigestUtils.md5Hex(newPassWord + account.getSalt());
        account.setPassword(password);
        accountRepository.save(account);

    }

    @Override
    @Transactional
    public void resetAccountPassWord(String accountId, String newPassWord) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new BizException("账号信息不存在"));
        String password = DigestUtils.md5Hex(newPassWord + account.getSalt());
        account.setPassword(password);
    }
}
