package com.atguigu.blog.controller;

import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.service.TUserService;
import com.atguigu.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 后台管理Controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TUserService userService;

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
    public String toAdminBlog(){
        return "admin/blogs";
    }
}
