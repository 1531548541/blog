package com.atguigu.blog.controller;

import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.service.TBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * 归档
 */
@Controller
public class ArchiveController {

    @Autowired
    private TBlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model, HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if(user!=null){
            model.addAttribute("archiveMap", blogService.archiveBlog(user.getId()));
            model.addAttribute("blogCount", blogService.countBlog(user.getId()));
            return "archives";
        }else {
            return "login";
        }
    }
}
