package com.example.demo.system.base;

import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import com.example.demo.system.service.UserService;
import com.example.demo.system.util.GenericsUtil;
import com.example.demo.system.util.SpringUtil;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

public abstract class AbstractBaseController<T, M> extends BaseController {
  private BaseService<T> service;

  public BaseService<T> getService() {
    if (service == null) {
      this.service = (BaseService) SpringUtil.getBean(GenericsUtil.getClassType(this.getClass(), 1));
    }
    return service;
  }

  @Autowired
  public UserService userService;
  public Example getExample() {
    return getService().getExample();
  }

  //    @GetMapping("selectById")
  public GenericResponse selectById(Integer id) {
    T t = selectByPrimaryKey(id);
    if (t == null) {
      return error("数据不存在");
    }
    return success(t);
  }

  //    @DeleteMapping("del")
  public GenericResponse del(Integer id) {
    getService().deleteByPrimaryKey(id);
    return success();
  }

  //   @PostMapping("edit")
  public GenericResponse edit(@RequestBody T t) {
    getService().updateByPrimaryKeySelective(t);
    return success();
  }

  // 通用方法  开始
  public T checkIdAndGet(Object id) {
    T t = selectByPrimaryKey(id);
    if (t == null) {
      throw new MyException(ResponseFormat.error(ErrorCode.DATA_NOT_EXISTS, "数据不存在"));
    }
    return t;
  }

  // 新增数据
  public int insert(T t) {
    return getService().insert(t);
  }

  public int insertSelective(T t) {
    return getService().insertSelective(t);
  }

  // 删除
  int deleteByPrimaryKey(Object id) {
    return getService().deleteByPrimaryKey(id);
  }

  // 改
  public int updateByPrimaryKey(T t) {
    return getService().updateByPrimaryKeySelective(t);
  }

  public int updateByPrimaryKeySelective(T t) {
    return getService().updateByPrimaryKeySelective(t);
  }

  public int updateByExampleSelective(T t, Object object) {
    return getService().updateByExampleSelective(t, object);
  }

  //查询
  public List<T> select(T t) {
    return getService().select(t);
  }

  public T selectOne(T t) {
    return getService().selectOne(t);
  }

  public T selectByPrimaryKey(Object id) {
    return getService().selectByPrimaryKey(id);
  }

  public List<T> selectAll() {
    return getService().selectAll();
  }

  public List<T> selectByExample(Object object) {
    return getService().selectByExample(object);
  }

  public int selectCount(T t) {
    return getService().selectCount(t);
  }

  public int selectCountByExample(Object object) {
    return getService().selectCountByExample(object);
  }
  // 通用方法  结束
}

