package com.cscec.mapper;

import org.springframework.stereotype.Repository;
import com.cscec.model.User;
import com.cscec.util.MyMapper;

@Repository
public interface UserMapper extends  MyMapper<User> {
}
