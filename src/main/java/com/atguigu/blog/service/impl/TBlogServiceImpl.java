package com.atguigu.blog.service.impl;

import com.atguigu.blog.VO.BlogQuery;
import com.atguigu.blog.constant.ConstantProperties;
import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.entity.TBlogTagMapping;
import com.atguigu.blog.entity.TTag;
import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.mapper.TBlogMapper;
import com.atguigu.blog.mapper.TBlogTagMappingMapper;
import com.atguigu.blog.mapper.TTypeMapper;
import com.atguigu.blog.mapper.TUserMapper;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TTagService;
import com.atguigu.blog.utils.MarkdownUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
            tBlog.setFirstPicture(ConstantProperties.FILE_URL+tBlog.getFirstPicture());
            //查找user
            TUser tUser = userMapper.selectById(tBlog.getUserId());
            tUser.setAvatar(ConstantProperties.FILE_URL+tUser.getAvatar());
            tBlog.setUser(tUser);
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
//        queryWrapper.eq("recommend",blogQuery.getRecommend()?1:0);
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

    @Override
    public TBlog getAndConvert(Long id) throws NotFoundException {
        TBlog blog = blogMapper.selectById(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        TUser tUser = userMapper.selectById(blog.getUserId());
        tUser.setAvatar(ConstantProperties.FILE_URL+tUser.getAvatar());
        blog.setUser(tUser);
        return blog;
    }

    @Override
    public Map<String, List<TBlog>> archiveBlog(Long userId) {
        Map<String, List<TBlog>> map=new HashMap<>();
        //先获取所有不同的years
        List<String> years= blogMapper.selectDiffentYears(userId);
        for (String year : years) {
            map.put(year,blogMapper.selectAllByYearAndUId(year,userId));
        }
        return map;
    }

    @Override
    public Integer countBlog(Long userId) {
        List<TBlog> blogs = blogMapper.selectList(new QueryWrapper<TBlog>().eq("user_id",userId));
        return blogs.size();
    }
}
