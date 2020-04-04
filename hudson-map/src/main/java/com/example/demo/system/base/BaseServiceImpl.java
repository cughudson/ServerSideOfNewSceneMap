package com.example.demo.system.base;

import com.example.demo.system.mapper.CommonMapper;
import com.example.demo.system.util.GenericsUtil;
import com.example.demo.system.util.MyMapper;
import com.example.demo.system.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
public abstract class BaseServiceImpl<T,M> implements  BaseService<T> {

    @Autowired
   public MyMapper<T> mapper;

    @Autowired
    private CommonMapper commonMapper;

   public MyMapper<T> mapper(){
       if(mapper ==null){
           this.mapper = (MyMapper) SpringUtil.getBean(GenericsUtil.getClassType(this.getClass(),1));
       }
       return mapper;
   }
    public Example getExample() {
        return new Example(GenericsUtil.getClassType(this.getClass(),0));
    }


    //增
    public int insert(T t) {
        return this.mapper.insert(t);
    }
    public int insertSelective(T t) {
        return this.mapper.insertSelective(t);
    }

    //删
    public int deleteByPrimaryKey(Object id){
        return this.mapper.deleteByPrimaryKey(id);
    }

    // 改
    public int updateByPrimaryKey(T t){
        return this.mapper.updateByPrimaryKey( t );
    }
    public int updateByPrimaryKeySelective(T t){
        return this.mapper.updateByPrimaryKeySelective( t );
    }
    public int updateByExampleSelective(T t,Object object){
        return this.mapper.updateByExampleSelective( t,object );
    }

    // 查询
    public List<T> select(T t){
        return this.mapper.select(t);
    }
    public T selectOne(T t){
        System.out.println(this);
        if(t instanceof Example){
            return this.mapper.selectOne(t);
        }
        return this.mapper.selectOneByExample(t);
    }
    public T selectByPrimaryKey(Object id){
        return this.mapper.selectByPrimaryKey(id);
    }
    public List<T> selectAll(){
        return this.mapper.selectAll();
    }
    public List<T> selectByExample(Object object){
        if(object instanceof Example){
            return this.mapper.selectByExample(object);
        }
        return this.mapper.select((T)object);
    }
    public int selectCount(T t){
        return this.mapper.selectCount(t);
    }
    public int selectCountByExample(Object object){
        return this.mapper.selectCountByExample(object);
    }
}
