package com.atguigu.blog.service.impl;

import com.atguigu.blog.VO.TagVO;
import com.atguigu.blog.entity.TTag;
import com.atguigu.blog.mapper.TTagMapper;
import com.atguigu.blog.service.TTagService;
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
public class TTagServiceImpl extends ServiceImpl<TTagMapper, TTag> implements TTagService {

    @Autowired
    private TTagMapper tagMapper;

    @Override
    public List<TagVO> listSomeAndCount(Integer countNum) {
        return tagMapper.selectSomeAndCount(countNum);
    }
}
