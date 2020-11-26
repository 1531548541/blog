package com.atguigu.blog.service.impl;

import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.entity.TBlogTagMapping;
import com.atguigu.blog.mapper.TBlogMapper;
import com.atguigu.blog.mapper.TBlogTagMappingMapper;
import com.atguigu.blog.mapper.TTypeMapper;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
@Service
@Transactional
public class TBlogServiceImpl extends ServiceImpl<TBlogMapper, TBlog> implements TBlogService {

    @Autowired
    private TBlogMapper blogMapper;

    @Autowired
    private TTypeMapper typeMapper;

    @Autowired
    private TTagService tagService;

    @Autowired
    private TBlogTagMappingMapper blogTagMappingMapper;

    @Override
    public List<TBlog> listAll() {
        return blogMapper.selectAll();
    }

    @Override
    public void saveBlog(TBlog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blogMapper.insert(blog);
        for (String s : blog.getTagIds().split(",")) {
            TBlogTagMapping tBlogTagMapping = new TBlogTagMapping();
            tBlogTagMapping.setBlogId(blog.getId());
            tBlogTagMapping.setTagId(Long.valueOf(s));
            blogTagMappingMapper.insert(tBlogTagMapping);
        }
    }

    @Override
    public TBlog getBlogById(Long id) {
        TBlog blog=blogMapper.selectById(id);
        blog.setType(typeMapper.selectById(blog.getTypeId()));
        QueryWrapper<TBlogTagMapping> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("blog_id",blog.getId());
        List<TBlogTagMapping> tBlogTagMappings = blogTagMappingMapper.selectList(queryWrapper);
        StringBuilder tagIds=new StringBuilder();
        for (TBlogTagMapping tBlogTagMapping : tBlogTagMappings) {
            tagIds.append(tBlogTagMapping.getTagId());
            tagIds.append(",");
        }
        tagIds.deleteCharAt(tagIds.length()-1);
        blog.setTagIds(tagIds.toString());
        return blog;
    }

    @Override
    public void updateByBlog(TBlog blog) {
        //修改blog
        blogMapper.updateById(blog);
        //修改tagList
        //先删除blogTagMapping表中数据
        QueryWrapper<TBlogTagMapping> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("blog_id",blog.getId());
        blogTagMappingMapper.delete(queryWrapper);
        //添加blogTagMapping表中数据
        for (String s : blog.getTagIds().split(",")) {
            blogTagMappingMapper.insert(new TBlogTagMapping().setBlogId(blog.getId()).setTagId(Long.valueOf(s)));
        }
    }
}
