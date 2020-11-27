package com.atguigu.blog.service.impl;

import com.atguigu.blog.VO.TypeVO;
import com.atguigu.blog.entity.TType;
import com.atguigu.blog.mapper.TTypeMapper;
import com.atguigu.blog.service.TTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TTypeServiceImpl extends ServiceImpl<TTypeMapper, TType> implements TTypeService {

    @Autowired
    private TTypeMapper typeMapper;

    @Override
    public List<TypeVO> listSomeAndCount(Integer countNum) {
        return typeMapper.selectSomeAndCount(countNum);
    }
}
