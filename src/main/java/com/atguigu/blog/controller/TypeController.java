package com.atguigu.blog.controller;


import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.entity.TType;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TTypeService;
import com.atguigu.blog.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class TypeController {

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TBlogService blogService;

    @Autowired
    private TUserService userService;

    @RequestMapping("/types/{id}")
    public String list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                       @PathVariable Integer id,
                       Model model){
        if(id==-1){
            //展示所有type
            List<TType> typeList = typeService.list(null);
            for (TType tType : typeList) {
                List<TBlog> blogList = blogService.list(new QueryWrapper<TBlog>().eq("type_id", tType.getId()));
                for (TBlog blog : blogList) {
                    blog.setUser(userService.getById(blog.getUserId()));
                    blog.setType(typeService.getById(blog.getTypeId()));
                }
                tType.setBlogs(blogList);
            }
            model.addAttribute("types",typeList);
        }else {
            //展示某一个type
            TType tType = typeService.getById(id);
            List<TBlog> blogList = blogService.list(new QueryWrapper<TBlog>().eq("type_id", tType.getId()));
            for (TBlog blog : blogList) {
                blog.setUser(userService.getById(blog.getUserId()));
                blog.setType(typeService.getById(blog.getTypeId()));
            }
            tType.setBlogs(blogList);
            model.addAttribute("types", tType);
        }
        //分页
        PageHelper.startPage(page,pageSize);
        List<TBlog> blogList = blogService.listAll();
        PageInfo pageInfo = new PageInfo(blogList);
        model.addAttribute("blogList",blogList);
        //查找六条记录
        model.addAttribute("recommendBlogs", blogService.list(null));
        model.addAttribute("page",pageInfo);
        return "types";
    }
}

