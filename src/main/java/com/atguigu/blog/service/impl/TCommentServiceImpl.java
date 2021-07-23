package com.atguigu.blog.service.impl;

import com.atguigu.blog.constant.ConstantProperties;
import com.atguigu.blog.entity.TComment;
import com.atguigu.blog.entity.TUser;
import com.atguigu.blog.mapper.TCommentMapper;
import com.atguigu.blog.mapper.TUserMapper;
import com.atguigu.blog.service.TCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements TCommentService {

    @Autowired
    private TCommentMapper commentMapper;

    @Autowired
    private TUserMapper userMapper;

    @Transactional
    @Override
    public void saveComment(TComment comment) {
        if (comment.getParentCommentId() == -1) {
            comment.setParentCommentId(null);
        }
        if(comment.getForeparentId()==-1){
            comment.setForeparentId(null);
        }
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    @Override
    public List<TComment> listCommentsByBlogId(Long blogId) {
        //找到所有评论
        QueryWrapper<TComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id",blogId);
        queryWrapper.isNull("parent_comment_id");
        queryWrapper.orderByDesc("create_time");
        List<TComment> comments = commentMapper.selectList(queryWrapper);
        for (TComment comment : comments) {
            //查找所有子评论（只分为两级）
            List<TComment> childs = commentMapper.selectList(new QueryWrapper<TComment>().eq("foreparent_id", comment.getId()));
            for (TComment child : childs) {
                child.setParentComment(commentMapper.selectById(child.getParentCommentId()));
                TUser tUser = userMapper.selectOne(new QueryWrapper<TUser>().eq("username", child.getUsername()));
                tUser.setAvatar(ConstantProperties.FILE_URL+tUser.getAvatar());
                child.setUser(tUser);
            }
            comment.setReplyComments(childs);
            TUser tUser = userMapper.selectOne(new QueryWrapper<TUser>().eq("username", comment.getUsername()));
            tUser.setAvatar(ConstantProperties.FILE_URL+tUser.getAvatar());
            comment.setUser(tUser);
        }
        return comments;
    }


}
