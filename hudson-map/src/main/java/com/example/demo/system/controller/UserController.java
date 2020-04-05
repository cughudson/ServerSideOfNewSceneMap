package com.example.demo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.base.BaseController;
import com.example.demo.system.entity.User;
import com.example.demo.system.entity.ext.UserExt;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.MyException;
import com.example.demo.system.util.Constant;
import com.example.demo.system.util.MD5Util;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(tags = "用户操作")
public class UserController extends BaseController {


    @PostMapping("/insert")
    @ApiOperation(value = "新增普通用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.username, value = "用户名", dataType = "String", required = true),
            @ApiImplicitParam(name = Constant.password, value = "密码", dataType = "String"),
            @ApiImplicitParam(name = Constant.headImg, value = "头像地址"),
            @ApiImplicitParam(name = "extra", value = "拓展"),
    })
    public GenericResponse insert(@RequestParam String username, String password, String headImg, String extra) throws MyException {
        if(! getUser().getAdmin()){
            throw new MyException(error(ErrorCode.PERMISSION_DENIED, "无权限的操作"));
        }
        if (userService.findByName(username) != null) {
            return error(ErrorCode.DATA_EXISTS, username + "已经存在");
        }

        if(StringUtils.isEmpty(password)){
            password=Constant.defaultPassword;
        }
        password= MD5Util.getPassword(password);
        User user= User.builder().username(username)
                .password(password)
                .admin(false)
                .del(false)
                .enable(true)
                .createTime(new Date())
                .updateTime(new Date())
                .headImg(headImg)
                .extra(extra)
                .build();
        userService.insert(user);
        user.setPassword(null);
        return success(user);

    }

    @PostMapping("/getHeadImgs")
    @ApiOperation("获取所有启用的头像")
    public GenericResponse getHeadImgs() {
        return success(commonService.selectEnableHeadImg());
    }

    @PostMapping("/list")
    @ApiOperation("查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.username, value = "名称"),
            @ApiImplicitParam(name = Constant.useable, value = "能否登陆", dataType = "boolean"),
            @ApiImplicitParam(name = Constant.del, value = "删除状态", dataType = "boolean"),
            @ApiImplicitParam(name = Constant.admin, value = "是否为管理员", dataType = "boolean"),
    })
    public GenericResponse list(String username, Boolean useable, Boolean del,Boolean admin) {
        User user = getUser();
        List<User> data = new ArrayList<>();
        if (user.getAdmin()) {
            data = userService.list(user,username, useable, del,admin);
            data.parallelStream().forEach(a -> a.setPassword(null));
        }
        return success(data);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.username, value = "用户名", required = true),
            @ApiImplicitParam(name = Constant.extra, value = "拓展"),
    })
    public GenericResponse update(@RequestParam Long id, @RequestParam String username,String extra) {

        if (id.intValue()!=getUser().getId().intValue() && ! getUser().getAdmin() ) {
            throw new MyException(error(ErrorCode.PERMISSION_DENIED, "无权限的操作"));
        }
        User user = userService.selectByPrimaryKey(id);
        if (user == null) {
            return error(ErrorCode.DATA_NOT_EXISTS, "id 无效");
        }
        if (!StringUtils.isEmpty(username)) {
            User tmp = userService.findByName(username);
            if (tmp != null && tmp.getId().intValue() != user.getId()) {
                return error(ErrorCode.DATA_NOT_EXISTS, username + "已存在");
            }
            user.setUsername(username);
        }
        if(!StringUtil.isNotEmpty(extra)){
           user.setExtra(extra);
        }
        userService.update(user);
        return success(user);
    }


    @PostMapping("/portrait/update")
    @ApiOperation(value = "修改头像")
    @ApiImplicitParam(name = Constant.name, value = "头像地址", required = true)
    public GenericResponse updateHeadImg(@RequestParam  String name) {
        UserExt user=getUser();
        user.setHeadImg(name);
        userService.update(user);
        updateSession(user);
        return success();
    }

    public void updateSession(UserExt user){
        request.getSession().setAttribute("user",user);
    }
    @PostMapping({"/updatePwd"})
    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newpwd", value = "新密码", required = true),
            @ApiImplicitParam(name = "oldpwd", value = "旧密码", required = true),
    })
    public GenericResponse updatePwd(@RequestParam String newpwd,@RequestParam String oldpwd) {
        if (newpwd.equals(oldpwd)) {
            return error(ErrorCode.PARAM_ERROR, "新旧密码不能相同");
        }
        User user =userService.selectByPrimaryKey(getUser().getId());
        if (user.getPassword().equals(MD5Util.getPassword( oldpwd))) {
            user.setPassword(MD5Util.getPassword(newpwd));
            userService.update(user);
            return success();
        } else {
            return error(ErrorCode.PARAM_ERROR, "原密码错误");
        }
    }

    @PostMapping("/resetPwd")
    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.password, value = "新密码"),
    })
    public GenericResponse resetPwd(Long id, String password) {
        User user = getUser();//判断是否有权限修改此人的密码
        boolean admin = user.getAdmin();
        if (!admin) {
            return error(ErrorCode.PERMISSION_DENIED, "权限不足,不能重置密码");
        }
        User updateUser = userService.selectByPrimaryKey(id);
        if (updateUser == null) {
            return error(ErrorCode.DATA_NOT_EXISTS, "用户不存在");
        }
        if(StringUtils.isEmpty(password)){
            password=Constant.defaultPassword;
        }
        updateUser.setPassword(MD5Util.getPassword( password));
        userService.update(updateUser);
        return success();
    }


    @PostMapping("/user")
    @ApiOperation(value = "当前登录用户信息")
    public GenericResponse user() {
        return success(getOutUser());
    }

    @ApiOperation(value = "获取token")
    @PostMapping("/getToken")
    public GenericResponse getToken(){
//        User user=userService.findById(getUser().getId());  //生成规则  用户id | 截止时间
        Long time=System.currentTimeMillis()/1000;
//        DesUtil.encode3Des(user.getPassword(),user.getId()+"|"+time) 本来想做加解密 但是没必要
        Long userId= getUser().getId();
        String token= MD5Util.getMd5(userId+"|"+ UUID.randomUUID());
        Long expireTime=time+Constant.tokenExpire;
        JSONObject result=new JSONObject();
        result.put("token",token);
        result.put("expire",expireTime);
        result.put(Constant.userId,userId);
        Constant.tokens.put(token,result);//有效期
        return success(result);
    }


    @PostMapping("/auth")
    @ApiOperation(value = "授予管理员")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse auth(Long id) {
//        updateUser(new User().setId(id).s(Constant.POWER_normal_admin));
        updateUser(User.builder().id(id).admin(true).build());
        return success();
    }

    @PostMapping("/unauth")
    @ApiOperation(value = "取消管理员")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse unAuth(Long id) {
        updateUser(User.builder().id(id).admin(false).build());
        return success();
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse del(Long id) {
        updateUser(User.builder().id(id).del(true).build());
        return success();
    }

    @PostMapping("/recover")
    @ApiOperation(value = "恢复删除状态")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse recover(Long id) {
        updateUser(User.builder().id(id).del(false).build());
        return success();
    }

    @PostMapping("/disable")
    @ApiOperation(value = "禁止登录")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse disable(Long id) {
        updateUser(User.builder().id(id).enable(false).build());
        return success();
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启用登录")
    @ApiImplicitParam(name = Constant.id, value = "用户id", required = true, dataType = "int")
    public GenericResponse enable(Long id) {
        updateUser(User.builder().id(id).enable(true).build());
        return success();
    }

    private void updateUser(User tmp) throws MyException {
        if (! getUser().getAdmin()) {
            throw new MyException(error(ErrorCode.PERMISSION_DENIED, "无权限的操作"));
        }
        if (tmp.getId() == null) {
            throw new RuntimeException("id 不能为空");
        }
        User user = userService.selectByPrimaryKey(tmp.getId());
        if (user == null) {
            throw new MyException(error(ErrorCode.PARAM_ERROR, "id 无效"));
        }
        userService.update(tmp);
    }

}
