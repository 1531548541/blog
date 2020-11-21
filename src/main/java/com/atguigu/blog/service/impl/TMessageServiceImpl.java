package com.atguigu.blog.service.impl;

import com.atguigu.blog.entity.TMessage;
import com.atguigu.blog.mapper.TMessageMapper;
import com.atguigu.blog.service.TMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TMessageServiceImpl extends ServiceImpl<TMessageMapper, TMessage> implements TMessageService {

}
