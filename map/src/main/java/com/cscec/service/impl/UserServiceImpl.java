package com.cscec.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.cscec.mapper.CommonMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cscec.mapper.UserMapper;
import com.cscec.model.User;
import com.cscec.service.UserService;
import com.cscec.util.Constant;
import com.cscec.util.UserUtil;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl  extends BaseServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Autowired
    private CommonMapper commonMapper;

    private static Random random = new Random();

    @Override
    public User findByName(String username) {
        Example example = new Example(User.class);
        example.and().andEqualTo(Constant.username, username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User findById(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void insert(User user) {
        user.setPassword(UserUtil.getPassword(user.getPassword()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        user.setDel(false);
        user.setEnable(true);
        if (StringUtils.isEmpty(user.getHeadImg())) {
            user.setHeadImg(Constant.headImgs.get(random.nextInt(Constant.headImgs.size())));
        }
        userMapper.insert(user);
    }

    @Override
    @Async
    public void updateLoginTime(User user) {
        user.setLastLoginTime(new Date());
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }


    @Override
    public List<User> list(User user, String username, Boolean enable, Boolean del,Boolean admin) {
        Example example = new Example(User.class);
        if (enable != null)
            example.and().andEqualTo(Constant.enable, enable);
        if (del != null)
            example.and().andEqualTo(Constant.del, del);
        if (admin!=null)
            example.and().andEqualTo(Constant.admin, del);
        if (!StringUtils.isEmpty(username))
            example.and().andLike(Constant.username, "%" + username + "%");

        example.orderBy(Constant.createTime).desc(); // 排序
        return userMapper.selectByExample(example);
    }


    @Override
    public void delCanDel(List<User> list) {
        Example example=new Example(User.class);
        List<Long> ids=new ArrayList<>();
        for (User user:list) {
            ids.add(user.getId());
        }
        example.and().andIn(Constant.id,ids);
        userMapper.deleteByExample(example);
    }

    private Example getCanDelExample(Integer days) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(Constant.del, true);
        criteria.andLessThan(Constant.delTime, LocalDateTime.now().plusDays(-days));
        return example;
    }
}
