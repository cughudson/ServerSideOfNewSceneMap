package com.cscec.service;

import java.util.List;

import com.cscec.model.User;

public interface UserService  {

	User findByName(String s);

	void updateLoginTime(User user);

	void update(User user);

	List<User> list(User user,String username, Boolean enable, Boolean del,Boolean admin);

	User findById(Long userId);

    void insert(User user);

	void delCanDel(List<User> list);

}
