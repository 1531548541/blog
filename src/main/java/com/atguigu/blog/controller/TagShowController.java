package com.atguigu.blog.controller;

import com.atguigu.blog.entity.*;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TBlogTagMappingService;
import com.atguigu.blog.service.TTagService;
import com.atguigu.blog.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TTagService tagService;

    @Autowired
    private TBlogService blogService;

    @Autowired
    private TUserService userService;

    @Autowired
    private TBlogTagMappingService blogTagMappingService;

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id, Model model) {
        if(id==-1){
            //展示所有type
            List<TTag> tagList = tagService.list(null);
            for (TTag tTag : tagList) {
                List<TBlog> blogList=new ArrayList<>();
                List<TBlogTagMapping> mappings = blogTagMappingService.list(new QueryWrapper<TBlogTagMapping>().eq("tag_id", tTag.getId()));
                for (TBlogTagMapping mapping : mappings) {
                    TBlog blog = blogService.getById(mapping.getBlogId());
                    if (blog != null) {
                        blog.setUser(userService.getById(blog.getUserId()));
                    }
                    blogList.add(blog);
                }
                tTag.setBlogs(blogList);
            }
            model.addAttribute("tags",tagList);
        }else {
            //展示某一个type
            TTag tag = tagService.getById(id);
            List<TBlog> blogList=new ArrayList<>();
            List<TBlogTagMapping> mappings = blogTagMappingService.list(new QueryWrapper<TBlogTagMapping>().eq("tag_id", tag.getId()));
            for (TBlogTagMapping mapping : mappings) {
                TBlog blog = blogService.getById(mapping.getBlogId());
                if(blog!=null){
                    List<TTag> tTags=new ArrayList<>();
                    blog.setUser(userService.getById(blog.getUserId()));
                    List<TBlogTagMapping> mappings1 = blogTagMappingService.list(new QueryWrapper<TBlogTagMapping>().eq("blog_id", blog.getId()));
                    for (TBlogTagMapping tBlogTagMapping : mappings1) {
                        TTag tag1 = tagService.getById(tBlogTagMapping.getTagId());
                        tTags.add(tag1);
                    }
                    blog.setTags(tTags);
                }
                blogList.add(blog);
            }
            tag.setBlogs(blogList);
            model.addAttribute("tags", tag);
        }
        return "tags";
    }
}
