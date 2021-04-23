package com.atguigu.blog.service.impl;

import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.mapper.TUserMapper;
import com.atguigu.blog.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

    @Autowired
    private TUserMapper userMapper;

    @Override
    public TUser checkUser(String username, String password) {
        QueryWrapper<TUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",password);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public TUser checkAdminUser(String username, String password) {
        QueryWrapper<TUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",password);
        queryWrapper.eq("type",0);
        return userMapper.selectOne(queryWrapper);
    }
}
