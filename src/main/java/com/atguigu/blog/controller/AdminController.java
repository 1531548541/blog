package com.atguigu.blog.controller;

import com.atguigu.blog.VO.BlogQuery;
import com.atguigu.blog.entity.*;
import com.atguigu.blog.service.*;
import com.atguigu.blog.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理Controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TUserService userService;

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TTagService tagService;

    @Autowired
    private TBlogService blogService;

    @Autowired
    private TBlogTagMappingService blogTagMappingService;

    @GetMapping
    public String toAdminLogin(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes){
        TUser user = userService.checkUser(username, MD5Utils.code(password));
        if(user!=null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            redirectAttributes.addFlashAttribute("message","用户名密码错误!");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    @GetMapping("/blogs")
    public String toAdminBlog(@RequestParam(value = "page",defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                              Model model){
        //分页
        PageHelper.startPage(page,pageSize);
        List<TBlog> blogList = blogService.listAll();
        PageInfo pageInfo = new PageInfo(blogList);
        model.addAttribute("blogList",blogList);
        model.addAttribute("typeList",typeService.list(null));
        model.addAttribute("page",pageInfo);
        model.addAttribute("total",pageInfo.getTotal());
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(@RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                        BlogQuery blogQuery, Model model) {
        //分页
        PageHelper.startPage(blogQuery.getPage(),pageSize);
        List<TBlog> blogList = blogService.searchByParm(blogQuery);
        PageInfo pageInfo = new PageInfo(blogList);
        model.addAttribute("blogList",blogList);
        model.addAttribute("page",pageInfo);
        model.addAttribute("total",pageInfo.getTotal());
        return "admin/blogs :: blogList";
    }


    //新增blog
    @PostMapping("/blogs")
    public String addBlog(TBlog blog,HttpSession session,RedirectAttributes attributes){
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
        return "redirect:/admin/blogs";
}

    //查看修改blog页面(回显)
    @GetMapping("/blogs/{id}")
    public String toEditBlog(@PathVariable Long id,Model model){
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("typeList", typeService.list(null));
        model.addAttribute("tagList", tagService.list(null));
        return "admin/blogs-input";
    }

    //查看新增blog页面
    @GetMapping("/blogs/input")
    public String toAddBlogs(Model model){
        model.addAttribute("blog", new TBlog());
        model.addAttribute("typeList", typeService.list(null));
        model.addAttribute("tagList", tagService.list(null));
        return "admin/blogs-input";
    }

    //删除blog
    @GetMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = blogService.removeBlogById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/blogs";
    }






    @GetMapping("/types")
    public String toAdminTypes(@RequestParam(value = "page",defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                Model model){
        //分页
        PageHelper.startPage(page,pageSize);
        List<TType> typeList = typeService.list(null);
        PageInfo pageInfo = new PageInfo(typeList);
        model.addAttribute("typeList",typeList);
        model.addAttribute("page",pageInfo);
        model.addAttribute("total",pageInfo.getTotal());
        return "admin/types";
    }

    //新增type
    @PostMapping("/types")
    public String addType(TType type,RedirectAttributes attributes){
        //根据name查找Type
        QueryWrapper<TType> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",type.getName());
        TType tType = typeService.getOne(queryWrapper);
        if(tType!=null){
            //存在
            attributes.addFlashAttribute("message","不能添加重复的分类");
            return "redirect:/admin/types/input";
        }else {
            //添加
            typeService.save(type);
            attributes.addFlashAttribute("message", "新增成功");
            return "redirect:/admin/types";
        }
    }

    //查看修改type页面(回显)
    @GetMapping("/types/{id}")
    public String toEditType(@PathVariable Long id,Model model){
        model.addAttribute("type", typeService.getById(id));
        return "admin/types-input";
    }

    //修改type
    @PostMapping("/types/update")
    public String editType(TType type,RedirectAttributes attributes){
        //根据name查找Type
        QueryWrapper<TType> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",type.getName());
        TType tType = typeService.getOne(queryWrapper);
        if(tType!=null){
            //存在
            attributes.addFlashAttribute("message","该分类已存在");
            return "redirect:/admin/types/"+tType.getId();
        }else {
            //添加
            typeService.updateById(type);
            attributes.addFlashAttribute("message", "修改成功");
            return "redirect:/admin/types";
        }
    }

    //查看新增type页面
    @GetMapping("/types/input")
    public String toAddTypes(Model model){
        model.addAttribute("type", new TType());
        return "admin/types-input";
    }

    //删除type
    @GetMapping("/types/delete/{id}")
    public String deleteType(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = typeService.removeById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/types";
    }




    @GetMapping("/tags")
    public String toAdminTags(@RequestParam(value = "page",defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                               Model model){
        //分页
        PageHelper.startPage(page,pageSize);
        List<TTag> tagList = tagService.list(null);
        PageInfo pageInfo = new PageInfo(tagList);
        model.addAttribute("tagList",tagList);
        model.addAttribute("page",pageInfo);
        model.addAttribute("total",pageInfo.getTotal());
        return "admin/tags";
    }

    //新增tag
    @PostMapping("/tags")
    public String addTag(TTag tag, RedirectAttributes attributes){
        //根据name查找Type
        QueryWrapper<TTag> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",tag.getName());
        TTag tTag = tagService.getOne(queryWrapper);
        if(tTag !=null){
            //存在
            attributes.addFlashAttribute("message","不能添加重复的标签");
            return "redirect:/admin/tags/input";
        }else {
            //添加
            tagService.save(tag);
            attributes.addFlashAttribute("message", "新增成功");
            return "redirect:/admin/tags";
        }
    }

    //查看修改tag页面(回显)
    @GetMapping("/tags/{id}")
    public String toEditTag(@PathVariable Long id,Model model){
        model.addAttribute("tag", tagService.getById(id));
        return "admin/tags-input";
    }

    //修改tag
    @PostMapping("/tags/update")
    public String editTag(TTag tage, RedirectAttributes attributes){
        //根据name查找Type
        QueryWrapper<TTag> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",tage.getName());
        TTag tTag = tagService.getOne(queryWrapper);
        if(tTag !=null){
            //存在
            attributes.addFlashAttribute("message","该分类已存在");
            return "redirect:/admin/tags/"+ tTag.getId();
        }else {
            //添加
            tagService.updateById(tage);
            attributes.addFlashAttribute("message", "修改成功");
            return "redirect:/admin/tags";
        }
    }

    //查看新增tag页面
    @GetMapping("/tags/input")
    public String toAddTags(Model model){
        model.addAttribute("tag", new TTag());
        return "admin/tags-input";
    }

    //删除tag
    @GetMapping("/tags/delete/{id}")
    public String deleteTag(@PathVariable Long id,RedirectAttributes attributes){
        boolean b = tagService.removeById(id);
        if(b){
            attributes.addFlashAttribute("message","删除成功");
        }else {
            attributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/tags";
    }
}
