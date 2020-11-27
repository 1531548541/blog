package com.atguigu.blog.service;

import com.atguigu.blog.VO.TypeVO;
import com.atguigu.blog.entity.TType;
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
public interface TTypeService extends IService<TType> {

    List<TypeVO> listSomeAndCount(Integer countNum);
}
