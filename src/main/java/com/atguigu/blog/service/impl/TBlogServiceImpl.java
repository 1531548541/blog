package com.atguigu.blog.service.impl;

import com.atguigu.blog.VO.BlogQuery;
import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.entity.TBlogTagMapping;
import com.atguigu.blog.entity.TTag;
import com.atguigu.blog.mapper.TBlogMapper;
import com.atguigu.blog.mapper.TBlogTagMappingMapper;
import com.atguigu.blog.mapper.TTypeMapper;
import com.atguigu.blog.mapper.TUserMapper;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private TUserMapper userMapper;

    @Autowired
    private TTypeMapper typeMapper;

    @Autowired
    private TTagService tagService;

    @Autowired
    private TBlogTagMappingMapper blogTagMappingMapper;

    @Override
    public List<TBlog> listAll() {
//        blogMapper.selectAll();
        List<TBlog> blogList = blogMapper.selectList(null);
        for (TBlog tBlog : blogList) {
            //查找user
            tBlog.setUser(userMapper.selectById(tBlog.getUserId()));
            //查找type
            tBlog.setType(typeMapper.selectById(tBlog.getTypeId()));
            //查找tagList
            List<TTag> tagList=new ArrayList<>();
            QueryWrapper<TBlogTagMapping> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("blog_id",tBlog.getId());
            List<TBlogTagMapping> blogTagMappingList = blogTagMappingMapper.selectList(queryWrapper);
            for (TBlogTagMapping tBlogTagMapping : blogTagMappingList) {
                //查找整个tag类
                TTag tag = tagService.getById(tBlogTagMapping.getTagId());
                tagList.add(tag);
            }
            tBlog.setTags(tagList);
        }
        return blogList;
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

    @Override
    public boolean removeBlogById(Long id) {
        //删除blog
        int i = blogMapper.deleteById(id);
        //删除blogtagMapping
        QueryWrapper<TBlogTagMapping> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("blog_id",id);
        int i1 = blogTagMappingMapper.delete(queryWrapper);
        if(i==0&&i1==0){
            return true;
        }
        return false;
    }

    @Override
    public List<TBlog> searchByParm(BlogQuery blogQuery) {
        QueryWrapper<TBlog> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("title",blogQuery.getTitle());
        if(blogQuery.getTypeId()!=null){
            queryWrapper.eq("type_id",blogQuery.getTypeId());
        }
        queryWrapper.eq("recommend",blogQuery.getRecommend()?1:0);
        List<TBlog> blogList = blogMapper.selectList(queryWrapper);
        for (TBlog tBlog : blogList) {
            //查找type
            tBlog.setType(typeMapper.selectById(tBlog.getTypeId()));
        }
        return blogList;
    }

    @Override
    public List<TBlog> listSomeRecommend(int num) {
        return blogMapper.selectSomeRecommend(num);
    }
}
