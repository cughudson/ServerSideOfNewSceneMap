package com.example.demo.system.service;

import com.example.demo.system.base.BaseService;
import com.example.demo.system.entity.Image;
import com.example.demo.system.entity.User;
import com.example.demo.vo.image.ImageBoundVO;
import java.util.List;

public interface ImageService extends BaseService<Image> {
  List<Image> bounds(ImageBoundVO boundVO);

  List<User> selectUsers();
}
