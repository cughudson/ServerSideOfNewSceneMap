package com.example.demo.system.controller;

import com.example.demo.system.base.IBaseController;
import com.example.demo.system.entity.Image;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.response.ResponseFormat;
import com.example.demo.system.service.ImageService;
import com.example.demo.system.util.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(tags = {"图片"})
@RestController
public class ImageController extends IBaseController<Image, ImageService> {

    @PostMapping("/insert")
    @ApiOperation(value = "新增图片")
    public GenericResponse insertE(@RequestBody Image image){
        if(getService().selectByPrimaryKey(image.getId())!=null){
            throw new MyException(error(ErrorCode.DATA_EXISTS, "该图片已经存在不能新增"));
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
    public GenericResponse edit(@RequestBody Image image) {
        checkPermission(image.getId());
        return success(getService().updateByPrimaryKeySelective(image));
    }

    @PostMapping("delete")
    @ApiOperation(value = "变更删除状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.id, value = "id", dataType = "String", required = true),
            @ApiImplicitParam(name = Constant.delete, value = "删除状态", dataType = "Boolean", required = true),
    })
    public GenericResponse delete(String id,Boolean delete){
        checkPermission(id);
        Image image=Image.builder().id(id).delete(delete).build();
        getService().updateByPrimaryKeySelective(image);
        return success(getService().selectByPrimaryKey(id));
    }


    @PostMapping("list")
    @ApiOperation(value = "查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.pageNum, value = "页码", defaultValue = "1", dataType = "int"),
            @ApiImplicitParam(name = Constant.pageSize, value = "每页大小",defaultValue = "10", dataType = "int"),
            @ApiImplicitParam(name = Constant.delete, value = "删除状态", dataType = "Boolean"),
    })
    public GenericResponse list(int pageNum,int pageSize,Boolean delete){
        Image image=Image.builder().delete(delete).build();
        PageHelper.startPage(pageNum,pageSize);
        List list=getService().select(image);
        PageInfo pageInfo=new PageInfo(list);
        return success(pageInfo);
    }

    public void checkPermission(String id){
        Image tmp=getService().selectByPrimaryKey(id);
        if(tmp==null){
            throw  new MyException(ResponseFormat.error(ErrorCode.DATA_NOT_EXISTS,"id 无效"));
        }
        if(!getUser().getAdmin() && !getUser().getId().equals(tmp.getUserId())){ //判断权限
            throw new MyException(error(ErrorCode.PERMISSION_DENIED, "无权限的操作"));
        }
    }

}
