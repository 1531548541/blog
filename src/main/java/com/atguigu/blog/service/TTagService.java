package com.atguigu.blog.service;

import com.atguigu.blog.VO.TagVO;
import com.atguigu.blog.entity.TTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
public interface TTagService extends IService<TTag> {

    List<TagVO> listSomeAndCount(Integer countNum);
}
