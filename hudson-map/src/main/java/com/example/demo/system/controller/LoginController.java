package com.example.demo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.base.BaseController;
import com.example.demo.system.entity.User;
import com.example.demo.system.entity.ext.UserExt;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.util.Constant;
import com.example.demo.system.util.MD5Util;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(tags = {"登录和注销"})
public class LoginController extends BaseController {
    private static final String logout_success = "退出成功";

    @PostMapping("/logout")
    @ApiOperation(value = "注销")
    public GenericResponse logout(HttpServletRequest request) {
        UserExt user = getUser();
        if(user !=null){
            Map<String, String> param = Maps.newHashMap();
            param.put(Constant.userId, String.valueOf(user.getId()));
            param.put(Constant.result, logout_success);
            param.put(Constant.username, user.getUsername());
            request.getSession().removeAttribute(Constant.user);
            cachedThreadPool.execute(() -> {
                JSONObject json = getLoginRecord(getRealIpAddress(request), Long.parseLong(param.get(Constant.userId)), 0, param.get(Constant.result), param.get(Constant.username));
                commonService.insertLoginRecord(json);
            });
        }
        return success("注销成功");
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.username, value = "姓名"),
            @ApiImplicitParam(name = Constant.password, value = "密码"),
            @ApiImplicitParam(name = Constant.token, value = Constant.token)
    })
    public GenericResponse login( HttpServletRequest request, String username, String password, String token) {

        String result = null;
        User user = null;
        int type = 0;
        try {
            if (!StringUtils.isEmpty(token)) {
                type = 1;
                if (!Constant.tokens.containsKey(token)) {
                    result = "非法token";
                    return error(ErrorCode.PARAM_ERROR, result);
                }
                JSONObject json = Constant.tokens.get(token);
                if (json.getLong("expire") < System.currentTimeMillis() / 1000) {
                    result = "token 已失效";
                    return error(ErrorCode.PARAM_ERROR, result);
                }
                user = userService.selectByPrimaryKey(json.getLong(Constant.userId));
            } else {
                if (username == null) {
                    result = "用户名不能为空";
                    return error(ErrorCode.PARAM_ERROR, result);
                }
                if (password == null) {
                    result = "密码不能为空";
                    return error(ErrorCode.PARAM_ERROR, result);
                }
                user = userService.findByName(username);
                if (user != null && !user.getPassword().equals(MD5Util.getPassword( password))) {
                    result = "密码错误";
                    return error(ErrorCode.USER_PWD_ERROR, result);
                }
            }
            GenericResponse genericResponse = loginSuccess(user);
            result = genericResponse.getMessage();
            if (!StringUtils.isEmpty(token) && Constant.tokens.containsKey(token)) {
                result += Constant.tokens.get(token).toJSONString();
                Constant.tokens.remove(token);
            }
            if(genericResponse.getCode()== ErrorCode.SUCCESS){
                JSONObject responseResult=new JSONObject();
                responseResult.put("user",getOutUser());
                responseResult.put("other","忘了，要看記錄");
                genericResponse= success(responseResult);
            }
            return genericResponse;
        } finally {
            Long userId = null;
            if (user != null) {
                userId = user.getId();
            }
            JSONObject json = getLoginRecord(getRealIpAddress(request), userId, type, result, username);
            commonService.insertLoginRecord(json);
        }
    }

    private GenericResponse loginSuccess(User user) {
        if (user == null || user.getDel()) {
            return error(ErrorCode.USER_NOT_EXISTS, "用户不存在");
        }
        if (!user.getEnable() || user.getDel()) {
            return error(ErrorCode.USER_CANNOT_LOGIN, "禁止登陆");
        }
        userService.updateLoginTime(user);//更新最后登陆时间
        UserExt userExt = new UserExt();
        BeanUtils.copyProperties(user, userExt);
        userExt.setRandomId(UUID.randomUUID().toString());//存入随机ID 用来做 只能登陆一次的限制
        Constant.userRandomMap.put(userExt.getId(), userExt);
        request.getSession().setAttribute(Constant.user, userExt);
        return success();
    }

}
