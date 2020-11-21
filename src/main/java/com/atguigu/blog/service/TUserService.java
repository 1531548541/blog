package com.atguigu.blog.service;

import com.atguigu.blog.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
public interface TUserService extends IService<TUser> {
    /**
     * 检查用户名密码
     */
    public TUser checkUser(String username,String password);
}
