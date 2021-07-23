package com.atguigu.blog.controller;

import com.atguigu.blog.entity.TComment;
import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.service.TBlogService;
import com.atguigu.blog.service.TCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private TCommentService commentService;

    @Autowired
    private TBlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentsByBlogId(blogId));
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(TComment comment, HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            comment.setUsername(user.getUsername());
            //判断是否是博主
            if(user.getId()==blogService.getById(comment.getBlogId()).getUserId()){
                comment.setAdminComment(true);
            }else {
                comment.setAdminComment(false);
            }
            commentService.saveComment(comment);
            return "redirect:/comments/" + comment.getBlogId();
        } else {
            return "login";
        }
    }

    //删除评论
    @PostMapping("/comment/delete")
    @ResponseBody
    public String deleteComment(String id) {
        if (StringUtils.isBlank(id)) {
            return "删除失败";
        }else {
            commentService.removeById(Long.parseLong(id));
            return "删除成功";
        }
    }

}
