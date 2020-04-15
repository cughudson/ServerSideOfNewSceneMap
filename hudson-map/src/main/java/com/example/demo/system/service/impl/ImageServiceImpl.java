package com.example.demo.system.service.impl;

import com.example.demo.system.base.BaseServiceImpl;
import com.example.demo.system.entity.Image;
import com.example.demo.system.entity.User;
import com.example.demo.system.mapper.ImageMapper;
import com.example.demo.system.mapper.UserMapper;
import com.example.demo.system.service.ImageService;
import com.example.demo.system.util.Constant;
import com.example.demo.vo.image.ImageBoundVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


@Service
@Transactional(rollbackFor = {Exception.class})
public class ImageServiceImpl extends BaseServiceImpl<Image, ImageMapper> implements ImageService {
  @Autowired
  private ImageMapper imageMapper;
  @Autowired
  private UserMapper userMapper;

  @Override
  public List<Image> bounds(ImageBoundVO boundVO) {
    Example example = getExample();
    ImageBoundVO.Bounds bounds = boundVO.getBounds();
    if (boundVO.getUserId() != null) {
      example.and().andEqualTo(Constant.userId, boundVO.getUserId());
    }
    if (boundVO.getDel() != null) {
      example.and().andEqualTo(Constant.delete, boundVO.getDel());
    }
    example.and().andBetween(Constant.blat, bounds.getWs().getLat(), bounds.getEn().getLat());
    example.and().andBetween(Constant.blng, bounds.getWs().getLng(), bounds.getEn().getLng());
    return getMapper().selectByExample(example);
  }

  @Override
  public List<User> selectUsers() {
    Example example = getExample();
    example.selectProperties(Constant.userId);
    example.setDistinct(true);
    Set<Long> ids = getMapper().selectByExample(example).stream().map(img -> img.getUserId()).collect(Collectors.toSet());
    List<Long> userIds = new ArrayList<>();
    ids.stream().forEach(a -> userIds.add(a));
    Example ex = new Example(User.class);
    ex.createCriteria().andIn(Constant.id, userIds);
    return userMapper.selectByExample(ex);
  }
}