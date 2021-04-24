package com.atguigu.blog.service;

import com.atguigu.blog.entity.TComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
public interface TCommentService extends IService<TComment> {

    void saveComment(TComment comment);

    List<TComment> listCommentsByBlogId(Long blogId);
}
