package com.atguigu.blog.controller;


import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TTagService;
import com.atguigu.blog.service.TTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
@Controller
public class BlogController {

    @Autowired
    private TBlogService blogService;

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TTagService tagService;

    @GetMapping(value = {"/","/index"})
    public String toIndex(@RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                          Model model){
        //分页
        PageHelper.startPage(page,pageSize);
        List<TBlog> blogList = blogService.listAll();
        PageInfo pageInfo = new PageInfo(blogList);
        model.addAttribute("blogList",blogList);
        //查找六条记录
        model.addAttribute("typeList", typeService.listSomeAndCount(6));
        model.addAttribute("tagList", tagService.listSomeAndCount(10));
        model.addAttribute("recommendBlogs", blogService.listSomeRecommend(6));
        model.addAttribute("page",pageInfo);
        return "index";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) throws NotFoundException {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

}

