package com.example.demo.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.base.AbstractBaseController;
import com.example.demo.system.entity.Image;
import com.example.demo.system.entity.User;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import com.example.demo.system.service.ImageService;
import com.example.demo.system.util.Constant;
import com.example.demo.vo.image.IdVO;
import com.example.demo.vo.image.ImageBoundVO;
import com.example.demo.vo.image.ImageCityVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;


@Api(tags = {"图片"})
@RestController
@RequestMapping("/image")
public class ImageController extends AbstractBaseController<Image, ImageService> {

  @Autowired
  private ImageService imageService;

  @PostMapping({"/image/bounds","bounds"})
  @ApiOperation(value = "根据指定范围查找图片")
  public GenericResponse bounds(@RequestBody ImageBoundVO boundVO) {
    PageHelper.startPage(1, boundVO.getNumber());
    List<Image>  list=imageService.bounds(boundVO);
    list.stream().forEach(image -> image.setUser(showUser(userService.selectByPrimaryKey(image.getUserId()))));
    return success(list);
  }

  @PostMapping({"/image/id","id"})
  @ApiOperation(value = "查找")
  public GenericResponse id(@RequestBody IdVO id) {
    Image image=checkIdAndGet(id.getId());
    image.setUser(showUser(userService.selectByPrimaryKey(image.getUserId())));
    return success(image);
  }

  public JSONObject showUser(User user){
    return JSON.parseObject(JSON.toJSONString(user)).fluentRemove(Constant.password);
  }

  @PostMapping("/insert")
  @ApiOperation(value = "新增图片")
  public GenericResponse insertE(@RequestBody Image image) {
    if (getService().selectByPrimaryKey(image.getId()) != null) {
      return error(ErrorCode.DATA_EXISTS, "该图片已经存在不能新增");
    }
    image.setUserId(getUser().getId());
    image.setUploader(getUser().getUsername());
    image.setCreateTime(new Date());
    image.setDelete(false);
    insert(image);
    return success(image);
  }

  @PostMapping("update")
  @ApiOperation(value = "编辑图片")
  @Override
  public GenericResponse edit(@RequestBody Image image) {
    checkPermission(image.getId());
    return success(getService().updateByPrimaryKeySelective(image));
  }

  @PostMapping("users")
  @ApiOperation(value = "获取用户")
  public GenericResponse users() {
    List<User> users = imageService.selectUsers();
    users.forEach(u -> u.setPassword(null));
    return success(users);
  }

  @PostMapping("delete")
  @ApiOperation(value = "变更删除状态")
  @ApiImplicitParams({
          @ApiImplicitParam(name = Constant.id, value = "id", dataType = "String", required = true),
          @ApiImplicitParam(name = Constant.delete, value = "删除状态", dataType = "Boolean", required = true),
  })
  public GenericResponse delete(String id, Boolean delete) {
    checkPermission(id);
    Image image = Image.builder().id(id).delete(delete).build();
    getService().updateByPrimaryKeySelective(image);
    return success(getService().selectByPrimaryKey(id));
  }

  @PostMapping("/getCity")
  @ApiOperation(value = "编辑图片")
  public GenericResponse getCity(@RequestBody ImageCityVO image) {
    JSONObject params = JSON.parseObject(JSON.toJSONString(image));
    Example example = getExample();
    Example.Criteria criteria = example.createCriteria();
    String userId = Constant.userId;
    if (params.containsKey(userId)) {
      criteria.andEqualTo(userId, params.get(userId));
    }
    if (!getUser().getAdmin()) {
      criteria.andEqualTo(userId, getUser().getId());
    }
    String province = Constant.province;
    String city = Constant.city;
    String county = Constant.county;

    checkAndAdd(params, criteria, province);
    checkAndAdd(params, criteria, city);
    checkAndAdd(params, criteria, county);
    example.selectProperties(province, city, county);
    example.setDistinct(true);
    return success(JSONArray.parseArray(JSON.toJSONString(getService().selectByExample(example))));
  }

  private void checkAndAdd(@RequestBody JSONObject params, Example.Criteria criteria, String attribute) {
    if (params.containsKey(attribute)) {
      criteria.andEqualTo(attribute, params.get(attribute));
    }
  }


  @PostMapping("list")
  @ApiOperation(value = "查询")
  @ApiImplicitParams({
          @ApiImplicitParam(name = Constant.pageNum, value = "页码", defaultValue = "1", dataType = "int"),
          @ApiImplicitParam(name = Constant.pageSize, value = "每页大小", defaultValue = Constant.defaultValue, dataType = "int"),
          @ApiImplicitParam(name = Constant.delete, value = "删除状态", dataType = "Boolean"),
          @ApiImplicitParam(name = Constant.userId,required = false, value = "用戶id", dataType = "int"),

  })
  public GenericResponse list(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", required = false, defaultValue = Constant.defaultValue) Integer pageSize,
                              Boolean delete,Integer userId) {
    PageHelper.startPage(pageNum, pageSize);
    Example example = getExample();
    Example.Criteria criteria= example.createCriteria();
    if(delete!=null){
      criteria.andEqualTo(Constant.delete, delete);
    }
    if(userId!=null){
      criteria.andEqualTo(Constant.userId, userId);
    }
    List<Image> list = getService().selectByExample(example);
    PageInfo pageInfo = new PageInfo(list);
    list.stream().forEach((i)->{
      i.setUser(showUser(userService.selectByPrimaryKey(i.getUserId())));
    });
    return success(pageInfo);
  }


  public void checkPermission(String id) {
    Image tmp = getService().selectByPrimaryKey(id);
    if (tmp == null) {
      throw new MyException(ResponseFormat.error(ErrorCode.DATA_NOT_EXISTS, "id 无效"));
    }
    if (!getUser().getAdmin() && !getUser().getId().equals(tmp.getUserId())) { //判断权限
      throw new MyException(error(ErrorCode.PERMISSION_DENIED, "无权限的操作"));
    }
  }

}
