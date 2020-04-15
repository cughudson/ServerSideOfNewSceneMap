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
   private MyMapper<T> mapper;

    @Autowired
    private CommonMapper commonMapper;

   public MyMapper<T> getMapper(){
       if(mapper ==null){
           this.mapper = (MyMapper) SpringUtil.getBean(GenericsUtil.getClassType(this.getClass(),1));
       }
       return mapper;
   }
   @Override
    public Example getExample() {
        return new Example(GenericsUtil.getClassType(this.getClass(),0));
    }


    //增
    @Override
    public int insert(T t) {
        return this.mapper.insert(t);
    }
    @Override
    public int insertSelective(T t) {
        return this.mapper.insertSelective(t);
    }

    //删
    @Override
    public int deleteByPrimaryKey(Object id){
        return this.mapper.deleteByPrimaryKey(id);
    }

    // 改
    @Override
    public int updateByPrimaryKey(T t){
        return this.mapper.updateByPrimaryKey( t );
    }
    @Override
    public int updateByPrimaryKeySelective(T t){
        return this.mapper.updateByPrimaryKeySelective( t );
    }
    @Override
    public int updateByExampleSelective(T t,Object object){
        return this.mapper.updateByExampleSelective( t,object );
    }

    // 查询
    @Override
    public List<T> select(T t){
        return this.mapper.select(t);
    }
    @Override
    public T selectOne(T t){
        if(t instanceof Example){
            return this.mapper.selectOne(t);
        }
        return this.mapper.selectOneByExample(t);
    }
    @Override
    public T selectByPrimaryKey(Object id){
        return this.mapper.selectByPrimaryKey(id);
    }
    @Override
    public List<T> selectAll(){
        return this.mapper.selectAll();
    }
    @Override
    public List<T> selectByExample(Object object){
        if(object instanceof Example){
            return this.mapper.selectByExample(object);
        }
        return this.mapper.select((T)object);
    }
    @Override
    public int selectCount(T t){
        return this.mapper.selectCount(t);
    }
    @Override
    public int selectCountByExample(Object object){
        return this.mapper.selectCountByExample(object);
    }
}
