package com.atguigu.blog.mapper;

import com.atguigu.blog.VO.TypeVO;
import com.atguigu.blog.entity.TType;
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
public interface TTypeMapper extends BaseMapper<TType> {

    List<TypeVO> selectSomeAndCount(Integer count);
}
