package org.learn.index12306.biz.userservice.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.index12306.biz.userservice.common.enums.UserChainMarkEnum;
import org.learn.index12306.biz.userservice.dao.entity.*;
import org.learn.index12306.biz.userservice.dao.mapper.*;
import org.learn.index12306.biz.userservice.dto.req.UserDeletionReqDTO;
import org.learn.index12306.biz.userservice.dto.req.UserLoginReqDTO;
import org.learn.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserLoginRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserRegisterRespDTO;
import org.learn.index12306.biz.userservice.service.UserLoginService;
import org.learn.index12306.biz.userservice.service.UserService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.starter.convention.exception.ServiceException;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainContext;
import org.learn.index12306.framework.starter.user.core.UserContext;
import org.learn.index12306.framework.starter.user.core.UserInfoDTO;
import org.learn.index12306.framework.starter.user.toolkit.JWTUtil;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.learn.index12306.biz.userservice.common.constant.RedisKeyConstant.*;
import static org.learn.index12306.biz.userservice.common.enums.UserRegisterErrorCodeEnum.*;
import static org.learn.index12306.biz.userservice.toolkit.UserReuseUtil.hashShardingIdx;

/**
 * 用户登录接口实现
 *
 * @author Milk
 * @version 2023/10/1 21:23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserMailMapper userMailMapper;
    private final UserPhoneMapper userPhoneMapper;
    private final UserReuseMapper userReuseMapper;
    private final UserDeletionMapper userDeletionMapper;
    private final RedissonClient redissonClient;
    private final DistributedCache distributedCache;
    private final AbstractChainContext<UserRegisterReqDTO> abstractChainContext;
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;


    @Override
    public Boolean hasUsername(String username) {
        // 先寻找布隆过滤器中是否存在，没有则一定不存在
        boolean hasUsername = userRegisterCachePenetrationBloomFilter.contains(username);
        // 如果存在，查看set是否该用户名已经被删除
        if(hasUsername){
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            return instance.opsForSet().isMember(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
        }
        return true;
    }


    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        String usernameOrMailOrPhone = requestParam.getUsernameOrMailOrPhone();
        boolean mailFlag = false;
        // 判断登录名是否是用邮箱登录
        for(char c : usernameOrMailOrPhone.toCharArray()){
            if(c == '@'){
                mailFlag = true;
                break;
            }
        }
        String username;
        // 通过邮箱查询用户名
        if(mailFlag){
            LambdaQueryWrapper<UserMailDO> queryWrapper = Wrappers.lambdaQuery(UserMailDO.class)
                    .eq(UserMailDO::getMail, usernameOrMailOrPhone);
            username = Optional.ofNullable(userMailMapper.selectOne(queryWrapper))
                    .map(UserMailDO::getUsername)
                    .orElseThrow(() -> new ClientException("用户名/手机号/邮箱不存在"));

        }else{
            // 通过手机号查询用户名
            LambdaQueryWrapper<UserPhoneDO> queryWrapper = Wrappers.lambdaQuery(UserPhoneDO.class)
                    .eq(UserPhoneDO::getPhone, usernameOrMailOrPhone);
            username = Optional.ofNullable(userPhoneMapper.selectOne(queryWrapper))
                    .map(UserPhoneDO::getUsername)
                    .orElse(null);
        }
        username = Optional.ofNullable(username).orElse(requestParam.getUsernameOrMailOrPhone());
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username)
                .eq(UserDO::getPassword, requestParam.getPassword())
                .select(UserDO::getId, UserDO::getUsername, UserDO::getRealName);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if(userDO != null){
            UserInfoDTO userInfo = UserInfoDTO.builder()
                    .userId(String.valueOf(userDO.getId()))
                    .username(userDO.getUsername())
                    .realName(userDO.getRealName())
                    .build();
            // 生成 JWT Token
            String accessToken = JWTUtil.generateAccessToken(userInfo);
            UserLoginRespDTO actual = new UserLoginRespDTO(userInfo.getUserId(), requestParam.getUsernameOrMailOrPhone()  , userDO.getRealName(), accessToken);
            distributedCache.put(accessToken, JSON.toJSONString(actual), 30, TimeUnit.MINUTES);
            return actual;
        }
        throw new ServiceException("账号不存在或密码错误");
    }


    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get(accessToken, UserLoginRespDTO.class);
    }

    @Override
    public void logout(String accessToken) {
        if(StrUtil.isNotBlank(accessToken)){
            distributedCache.delete(accessToken);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        // 责任链模式验证注册用户请求参数是否合规
        abstractChainContext.handler(UserChainMarkEnum.USER_REGISTER_FILTER.name(),requestParam);
        // 当前的线程获取锁
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER + requestParam.getUsername());
        boolean tryLock = lock.tryLock();
        // 获取失败表示已经获取过锁
        if(!tryLock){
            throw new ServiceException(HAS_USERNAME_NOTNULL);
        }

        try{
            try{
                int inserted = userMapper.insert(BeanUtil.convert(requestParam, UserDO.class));
                if(inserted < 1){
                    throw new ServiceException(USER_REGISTER_FAIL);
                }
            }catch(DuplicateKeyException dke){
                log.error("用户名 [{}] 重复注册", requestParam.getUsername());
                throw new ServiceException(HAS_USERNAME_NOTNULL);
            }
            UserPhoneDO userPhoneDO = UserPhoneDO.builder()
                    .phone(requestParam.getPhone())
                    .username(requestParam.getUsername())
                    .build();
            // 将用户名和电话匹配，防止在分库分表的情况下查询所有的物理表，转而查询一个用户名和手机号对应的表即可
            try{
                userPhoneMapper.insert(userPhoneDO);
            }catch(DuplicateKeyException dke){
                log.error("用户 [{}] 注册手机号 [{}] 重复", requestParam.getUsername(), requestParam.getPhone());
                throw new ServiceException(PHONE_REGISTERED);
            }
            // 同手机号
            if(StrUtil.isNotBlank(requestParam.getMail())){
                UserMailDO userMailDO = UserMailDO.builder()
                        .mail(requestParam.getMail())
                        .username(userPhoneDO.getUsername())
                        .build();
                try{
                    userMailMapper.insert(userMailDO);
                }catch (DuplicateKeyException dke) {
                    log.error("用户 [{}] 注册邮箱 [{}] 重复", requestParam.getUsername(), requestParam.getMail());
                    throw new ServiceException(MAIL_REGISTERED);
                }
            }
            String username = requestParam.getUsername();
            // 将使用过的用户名移除数据库， 表示已经被使用
            userReuseMapper.delete(Wrappers.update(new UserReuseDO(username)));
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            // 将使用过的用户名移除 set 中，表示已经被使用
            instance.opsForSet().remove(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);
            // 将使用过的用户名加入布隆过滤器中
            userRegisterCachePenetrationBloomFilter.add(username);
        }finally{
            lock.unlock();
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletion(UserDeletionReqDTO requestParam) {
        String username = UserContext.getUsername();
        if(!Objects.equals(username, requestParam.getUsername())){
            // 需要异常检测
            throw new ClientException("注销账号与登录账号不一致");
        }
        // 当前的线程获取锁
        RLock lock = redissonClient.getLock(USER_DELETION + requestParam.getUsername());
        lock.lock();
        try{
            UserQueryRespDTO userQueryRespDTO = userService.queryUserByUsername(requestParam.getUsername());
            UserDeletionDO userDeletionDO = UserDeletionDO.builder()
                    .idType(userQueryRespDTO.getIdType())
                    .idCard(userQueryRespDTO.getIdCard())
                    .build();
            // 删除时插入表中，方便记录相关证件注销次数，防止恶意注销
            userDeletionMapper.insert(userDeletionDO);
            // 逻辑删除用户
            UserDO userDO = new UserDO();
            userDO.setDeletionTime(System.currentTimeMillis());
            userDO.setUsername(username);
            userMapper.deletionUser(userDO);
            // 逻辑删除用户姓名和手机号对应的关系
            UserPhoneDO userPhoneDO = UserPhoneDO.builder()
                    .phone(userQueryRespDTO.getPhone())
                    .deletionTime(System.currentTimeMillis())
                    .build();
            userPhoneMapper.deletionUser(userPhoneDO);
            // 逻辑删除用户姓名和邮箱对应的关系
            if(StrUtil.isNotBlank(userQueryRespDTO.getMail())){
                UserMailDO userMailDO = UserMailDO.builder()
                        .mail(userQueryRespDTO.getMail())
                        .deletionTime(System.currentTimeMillis())
                        .build();
                userMailMapper.deletionUser(userMailDO);
            }
            // 删除 Token
            distributedCache.delete(UserContext.getToken());
            // 操作数据库和缓存，表明该用户名可以复用
            userReuseMapper.insert(new UserReuseDO(username));
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            instance.opsForSet().add(USER_REGISTER_REUSE_SHARDING + hashShardingIdx(username), username);

        }finally{
            lock.unlock();
        }
    }
}
