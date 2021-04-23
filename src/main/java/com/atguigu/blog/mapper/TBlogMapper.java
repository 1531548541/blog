package com.atguigu.blog.mapper;

import com.atguigu.blog.entity.TBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
public interface TBlogMapper extends BaseMapper<TBlog> {

    List<TBlog> selectAll();

    List<TBlog> selectSomeRecommend(int num);

    List<String> selectDiffentYears();

    List<TBlog> selectAllByYear(String year);
}
