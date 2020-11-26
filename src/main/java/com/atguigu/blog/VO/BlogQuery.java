package com.atguigu.blog.VO;

import lombok.Data;

@Data
public class BlogQuery {
    private Integer page;
    private String title="";
    private Long typeId;
    private Boolean recommend;
}
