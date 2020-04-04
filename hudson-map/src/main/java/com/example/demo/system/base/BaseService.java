package com.example.demo.system.base;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

public interface BaseService<T> {
    // 新增数据
    int insert(T t);
    int insertSelective(T t);

    // 删除
    int  deleteByPrimaryKey(Object id);
    // 改
    int updateByPrimaryKey(T t);
    int updateByPrimaryKeySelective(T t);
    int updateByExampleSelective(T t, Object object);
    //查询
    List<T> select(T t);
    T selectOne(T t);
    T selectByPrimaryKey(Object id);
    List<T> selectAll();
    List<T> selectByExample(Object object);
    int selectCount(T t);
    int selectCountByExample(Object object);

    Example getExample();
}
