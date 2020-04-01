package com.example.demo.system.base;

import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class IBaseController<T>  extends  BaseController{
    @Autowired
    public  BaseService<T> service;
//    @GetMapping("selectById")
    public GenericResponse selectById(Integer id){
        T t=selectByPrimaryKey(id);
        if(t==null){
            return error("数据不存在");
        }
        return success(t);
    }
//    @DeleteMapping("del")
    public GenericResponse del(Integer id){
        this.service.deleteByPrimaryKey(id);
        return success();
    }

//   @PostMapping("edit")
    public GenericResponse edit(@RequestBody T t){
        this.service.updateByPrimaryKeySelective(t);
        return success();
    }

    // 通用方法  开始
    public T checkIdAndGet(Integer id) {
        T t=selectByPrimaryKey(id);
        if(t==null){
            throw new MyException(ResponseFormat.error(ErrorCode.DATA_NOT_EXISTS,"数据不存在"));
        }
        return t;
    }
    // 新增数据
    public int insert(T t){
        return this.service.insert(t);
    }
    public int insertSelective(T t){
        return this.service.insertSelective(t);
    }

    // 删除
    int  deleteByPrimaryKey(Object id){
        return this.service.deleteByPrimaryKey(id);
    }
    // 改
    public int updateByPrimaryKey(T t){
        return this.service.updateByPrimaryKeySelective(t);
    }
    public int updateByPrimaryKeySelective(T t){
        return this.service.updateByPrimaryKeySelective(t);
    }
    public int updateByExampleSelective(T t,Object object){
        return this.service.updateByExampleSelective(t,object);
    }
    //查询
    public List<T> select(T t){
        return this.service.select(t);
    }
    public T selectOne(T t){
        return this.service.selectOne(t);
    }
    public T selectByPrimaryKey(Object id){
        return this.service.selectByPrimaryKey(id);
    }
    public List<T> selectAll(){
        return this.service.selectAll();
    }
    public List<T> selectByExample(Object object){return this.service.selectByExample(object);}
    public int selectCount(T t){
        return this.service.selectCount(t);
    }
    public int selectCountByExample(Object object){
        return this.service.selectCountByExample(object);
    }
    // 通用方法  结束
}

