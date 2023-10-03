package org.learn.index12306.biz.userservice.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.userservice.dao.entity.UserDO;
import org.learn.index12306.biz.userservice.dao.entity.UserDeletionDO;
import org.learn.index12306.biz.userservice.dao.entity.UserMailDO;
import org.learn.index12306.biz.userservice.dao.mapper.UserDeletionMapper;
import org.learn.index12306.biz.userservice.dao.mapper.UserMailMapper;
import org.learn.index12306.biz.userservice.dao.mapper.UserMapper;
import org.learn.index12306.biz.userservice.dto.req.UserUpdateReqDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryActualRespDTO;
import org.learn.index12306.biz.userservice.dto.resp.UserQueryRespDTO;
import org.learn.index12306.biz.userservice.service.UserService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Milk
 * @version 2023/10/1 21:24
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserMailMapper userMailMapper;
    private final UserDeletionMapper userDeletionMapper;


    @Override
    public UserQueryRespDTO queryUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if(userDO == null){
            throw new ClientException("用户不存在，请检查用户ID是否正确");
        }

        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
    }

    @Override
    public UserQueryActualRespDTO queryActualUserByUsername(String username) {
        return BeanUtil.convert(queryUserByUsername(username), UserQueryActualRespDTO.class);
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        UserQueryRespDTO userQueryRespDTO = queryUserByUsername(requestParam.getUsername());
        UserDO userDO = BeanUtil.convert(requestParam, UserDO.class);
        LambdaQueryWrapper<UserDO> userQueryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        // 更新用户信息
        userMapper.update(userDO, userQueryWrapper);
        // 同时更新邮箱
        if(StrUtil.isNotBlank(requestParam.getMail()) && !Objects.equals(requestParam.getMail(), userQueryRespDTO.getMail())){
            LambdaQueryWrapper<UserMailDO> userMailQueryWrapper = Wrappers.lambdaQuery(UserMailDO.class)
                    .eq(UserMailDO::getMail, userQueryRespDTO.getMail());
            userMailMapper.delete(userMailQueryWrapper);
            // 需要先删除然后更新
            UserMailDO userMailDO = UserMailDO.builder()
                    .mail(requestParam.getMail())
                    .username(requestParam.getUsername())
                    .build();
            userMailMapper.insert(userMailDO);
        }
    }

    @Override
    public Integer queryUserDeletionNum(Integer idType, String idCard) {
        LambdaQueryWrapper<UserDeletionDO> queryWrapper = Wrappers.lambdaQuery(UserDeletionDO.class)
                .eq(UserDeletionDO::getIdType, idType)
                .eq(UserDeletionDO::getIdCard, idCard);

        // TODO 此处应该先查缓存
        Long deletionCount = userDeletionMapper.selectCount(queryWrapper);
        return Optional.ofNullable(deletionCount).map(Long::intValue).orElse(0);
    }
}
