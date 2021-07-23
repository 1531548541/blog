package com.atguigu.blog.controller;


import com.atguigu.blog.constant.ConstantProperties;
import com.atguigu.blog.entity.TBlog;
import com.atguigu.blog.entity.TUser;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
        TBlog blog = blogService.getAndConvert(id);
        blog.setFirstPicture(ConstantProperties.FILE_URL+blog.getFirstPicture());
        model.addAttribute("blog", blog);
        return "blog";
    }

    //新增blog
    @PostMapping("/blog")
    public String addBlog(TBlog blog, HttpSession session, RedirectAttributes attributes){
        TUser user = (TUser) session.getAttribute("user");
        if(user!=null){
            blog.setUserId(user.getId());
        }
        if(blog.getId()==null){
            //添加
            blogService.saveBlog(blog);
            attributes.addFlashAttribute("message", "新增成功");
        }else{
            //修改
            blogService.updateByBlog(blog);
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/index";
    }

    //查看修改blog页面(回显)
    @GetMapping("/updateBlog/{id}")
    public String toEditBlog(@PathVariable Long id,Model model){
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("typeList", typeService.list(null));
        model.addAttribute("tagList", tagService.list(null));
        return "blogs-input";
    }

    //查看新增blog页面
    @GetMapping("/blog/input")
    public String toAddBlogs(Model model,HttpSession session){
        TUser user = (TUser) session.getAttribute("user");
        if(user!=null){
            model.addAttribute("blog", new TBlog());
            model.addAttribute("typeList", typeService.list(null));
            model.addAttribute("tagList", tagService.list(null));
            return "blogs-input";
        }else {
            return "login";
        }
    }

    //删除blog
    @GetMapping("/blog/delete/{id}")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = blogService.removeBlogById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/index";
    }

}

