package com.example.demo.system.service.impl;


import com.example.demo.system.base.BaseServiceImpl;
import com.example.demo.system.entity.Image;
import com.example.demo.system.mapper.ImageMapper;
import com.example.demo.system.service.ImageService;
import com.example.demo.system.util.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { Exception.class})
public class ImageServiceImpl extends BaseServiceImpl<Image> implements ImageService {
    @Autowired
    public ImageMapper mapper;

    @Override
    public MyMapper<Image> getMapper() {
        return mapper;
    }
}