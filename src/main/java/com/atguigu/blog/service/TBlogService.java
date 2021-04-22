package com.atguigu.blog.service;

import com.atguigu.blog.VO.BlogQuery;
import com.atguigu.blog.entity.TBlog;
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
public interface TBlogService extends IService<TBlog> {

    List<TBlog> listAll();

    void saveBlog(TBlog blog);

    TBlog getBlogById(Long id);

    void updateByBlog(TBlog blog);

    boolean removeBlogById(Long id);

    List<TBlog> searchByParm(BlogQuery blogQuery);

    List<TBlog> listSomeRecommend(int num);
}
