package com.example.demo.system.service.impl;

import com.example.demo.system.base.BaseServiceImpl;
import com.example.demo.system.entity.User;
import com.example.demo.system.mapper.UserMapper;
import com.example.demo.system.service.UserService;
import com.example.demo.system.util.Constant;
import com.example.demo.system.util.MD5Util;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserMapper> implements UserService {

  private static Random random = new Random();
  @Autowired
  private UserMapper mapper;

  @Override
  public User findByName(String username) {
    Example example = new Example(User.class);
    example.and().andEqualTo(Constant.username, username);
    return mapper.selectOneByExample(example);
  }

  @Override
  public int insert(User user) {
    user.setPassword(MD5Util.getPassword(user.getPassword()));
    user.setCreateTime(new Date());
    user.setUpdateTime(new Date());
    user.setLastLoginTime(new Date());
    user.setDel(false);
    user.setEnable(true);
    if (StringUtils.isEmpty(user.getHeadImg())) {
      user.setHeadImg(Constant.headImgs.get(random.nextInt(Constant.headImgs.size())));
    }
    return mapper.insert(user);
  }

  @Override
  @Async
  public void updateLoginTime(User user) {
    user.setLastLoginTime(new Date());
    mapper.updateByPrimaryKey(user);
  }

  @Override
  public void update(User user) {
    user.setUpdateTime(new Date());
    mapper.updateByPrimaryKeySelective(user);
  }


  @Override
  public List<User> list(User user, String username, Boolean enable, Boolean del, Boolean admin) {
    Example example = new Example(User.class);
    if (enable != null) {
      example.and().andEqualTo(Constant.enable, enable);
    }
    if (del != null) {
      example.and().andEqualTo(Constant.del, del);
    }
    if (admin != null) {
      example.and().andEqualTo(Constant.admin, admin);
    }
    if (!StringUtils.isEmpty(username)) {
      example.and().andLike(Constant.username, "%" + username + "%");
    }
    example.orderBy(Constant.createTime).desc(); // 排序
    return mapper.selectByExample(example);
  }

  @Override
  public void delCanDel(List<User> list) {
    Example example = new Example(User.class);
    List<Long> ids = new ArrayList<>();
    for (User user : list) {
      ids.add(user.getId());
    }
    example.and().andIn(Constant.id, ids);
    mapper.deleteByExample(example);
  }

  private Example getCanDelExample(Integer days) {
    Example example = new Example(User.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo(Constant.del, true);
    criteria.andLessThan(Constant.delTime, LocalDateTime.now().plusDays(-days));
    return example;
  }
}
