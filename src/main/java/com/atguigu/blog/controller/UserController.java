package com.atguigu.blog.controller;


import com.atguigu.blog.constant.ConstantProperties;
import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.service.TUserService;
import com.atguigu.blog.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private TUserService userService;

    @GetMapping("/login")
    public String toAdminLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        TUser user = userService.checkUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "redirect:/index";
        } else {
            redirectAttributes.addFlashAttribute("message", "用户名密码错误!");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }

    //注册
    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(TUser user,RedirectAttributes redirectAttributes) {
        //判断用户名是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        TUser tUser = userService.getOne(queryWrapper);
        if (tUser != null) {
            redirectAttributes.addFlashAttribute("message", "用户名已存在!");
            return "redirect:/register";
        } else {
            //添加
            user.setType(1);
            user.setPassword(MD5Utils.code(user.getPassword()));
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userService.save(user);
            return "redirect:/login";
        }
    }

    //个人信息管理
    @GetMapping("/myInfo")
    public String myInfo(HttpSession session, Model model) {
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            TUser tUser = userService.getById(user.getId());
            tUser.setAvatar(ConstantProperties.FILE_URL+tUser.getAvatar());
            model.addAttribute("user", tUser);
            return "myInfo";
        } else {
            return "login";
        }
    }

    //修改个人信息
    @PostMapping("/myInfo")
    public String updateMyInfo(TUser user, HttpSession session, RedirectAttributes attributes) {
        //修改
        user.setUpdateTime(new Date());
        userService.updateById(user);
        attributes.addFlashAttribute("message", "修改成功");
        //更新session
        TUser tUser = userService.getById(user.getId());
        tUser.setPassword(null);
        session.setAttribute("user", tUser);
        return "redirect:/index";
    }
}

