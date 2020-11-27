package com.atguigu.blog.mapper;

import com.atguigu.blog.VO.TagVO;
import com.atguigu.blog.entity.TTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wujie
 * @since 2020-11-16
 */
public interface TTagMapper extends BaseMapper<TTag> {

    List<TagVO> selectSomeAndCount(Integer count);
}
