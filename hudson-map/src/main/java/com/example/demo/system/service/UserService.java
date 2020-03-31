package com.example.demo.system.service;


import com.example.demo.system.base.BaseService;
import com.example.demo.system.entity.User;

import java.util.List;

public interface UserService extends BaseService<User> {

	User findByName(String s);

	void updateLoginTime(User user);

	int insert(User user);

	void update(User user);

	List<User> list(User user, String username, Boolean enable, Boolean del, Boolean admin);

	void delCanDel(List<User> list);

}
