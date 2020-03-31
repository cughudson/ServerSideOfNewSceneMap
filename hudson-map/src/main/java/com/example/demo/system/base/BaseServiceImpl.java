package com.example.demo.system.base;

import com.example.demo.system.util.MyMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

public abstract class BaseServiceImpl<T> implements  BaseService<T> {

    public abstract MyMapper<T> getMapper();

    //增
    public int insert(T t) {
        return this.getMapper().insert(t);
    }
    public int insertSelective(T t) {
        return this.getMapper().insertSelective(t);
    }

    //删
    public int deleteByPrimaryKey(Object id){
        return this.getMapper().deleteByPrimaryKey(id);
    }

    // 改
    public int updateByPrimaryKey(T t){
        return this.getMapper().updateByPrimaryKey( t );
    }
    public int updateByPrimaryKeySelective(T t){
        return this.getMapper().updateByPrimaryKeySelective( t );
    }
    public int updateByExampleSelective(T t,Object object){
        return this.getMapper().updateByExampleSelective( t,object );
    }

    // 查询
    public List<T> select(T t){
        return this.getMapper().select(t);
    }
    public T selectOne(T t){
        if(t instanceof Example){
            return this.getMapper().selectOne(t);
        }
        return this.getMapper().selectOneByExample(t);
    }
    public T selectByPrimaryKey(Object id){
        return this.getMapper().selectByPrimaryKey(id);
    }
    public List<T> selectAll(){
        return this.getMapper().selectAll();
    }
    public List<T> selectByExample(Object object){
        if(object instanceof Example){
            return this.getMapper().selectByExample(object);
        }
        return this.getMapper().select((T)object);
    }
    public int selectCount(T t){
        return this.getMapper().selectCount(t);
    }
    public int selectCountByExample(Object object){
        return this.getMapper().selectCountByExample(object);
    }
}
