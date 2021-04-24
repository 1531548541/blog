package com.atguigu.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String content;

    private Date createTime;

    private Long blogId;

    private Long parentCommentId;

    private Boolean adminComment;

    private Long foreparentId;

    @TableField(exist = false)
    private TBlog blog;

    @TableField(exist = false)
    private TUser user;

    @TableField(exist = false)
    private List<TComment> replyComments = new ArrayList<>();

    @TableField(exist = false)
    private TComment parentComment;
}
