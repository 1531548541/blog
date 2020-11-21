package com.atguigu.blog.service.impl;

import com.atguigu.blog.entity.TComment;
import com.atguigu.blog.mapper.TCommentMapper;
import com.atguigu.blog.service.TCommentService;
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
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements TCommentService {

}
