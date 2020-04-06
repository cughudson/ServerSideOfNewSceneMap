package com.example.demo.system.service.impl;


import com.example.demo.system.base.BaseServiceImpl;
import com.example.demo.system.entity.Image;
import com.example.demo.system.mapper.ImageMapper;
import com.example.demo.system.service.ImageService;
import com.example.demo.system.util.Constant;
import com.example.demo.system.util.StringUtils;
import com.example.demo.vo.image.ImageBoundVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(rollbackFor = { Exception.class})
public class ImageServiceImpl extends BaseServiceImpl<Image,ImageMapper> implements ImageService {
    @Override
    public List<Image> bounds(ImageBoundVO boundVO) {
        Example example=getExample();
        ImageBoundVO.Bounds bounds=boundVO.getBounds();
        if(boundVO.getUserId() !=null){
            example.and().andEqualTo(Constant.userId,boundVO.getUserId());
        }
        example.and().andBetween(Constant.blat,bounds.getWs().getLat(),bounds.getEn().getLat());
        example.and().andBetween(Constant.blng,bounds.getWs().getLng(),bounds.getEn().getLng());
        return getMapper().selectByExample(example);
    }
}