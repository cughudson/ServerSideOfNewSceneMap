package com.cscec.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cscec.util.response.ErrorCode;
import com.cscec.util.response.GenericResponse;
import com.cscec.util.response.ResponseFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSONObject;
import com.cscec.model.ext.UserExt;
import com.cscec.service.CommonService;
import com.cscec.service.UserService;
import com.cscec.util.Constant;

public abstract class BaseController {
    public Logger logger= LoggerFactory.getLogger(this.getClass());

    public DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Value("${img-save-path}")
    public String savePath;
    @Autowired
    public CommonService commonService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    public UserService userService;


    public UserExt getUser() {
        return (UserExt) request.getSession().getAttribute(Constant.user);
    }
    public JSONObject getJSON() {
        return new JSONObject();
    }
    public String getFileType(String format) {
        return String.format("%s/%s/",Constant.projectName,format);
    }

    public JSONObject getLoginRecord(String ip,Long userId, int type,String result, String username) {
        JSONObject json=new JSONObject();
        json.put(Constant.ip,ip);
        json.put(Constant.userId,userId);
        json.put(Constant.type,type);
        json.put(Constant.result,result);
        json.put(Constant.username,username);
        json.put(Constant.loginTime,new Date());
        return  json;
    }
    public Map<String, Object> paramMapToHashMap(Map<String, String[]> parameterMap) {
        Map<String, Object> result=new HashMap<>();
        for (String key:parameterMap.keySet()) {
            if(parameterMap.get(key).length==1){
                result.put(key,parameterMap.get(key)[0]);
            }else if(parameterMap.get(key).length >0){
                result.put(key,parameterMap.get(key));
            }
        }
        result.remove(Constant.pageNum);
        result.remove(Constant.pageSize);
        return result;
    }

    public GenericResponse error(int status, String message) {
        return ResponseFormat.error(status,message);
    }
    public GenericResponse error( String message) {
        return ResponseFormat.error(ErrorCode.PARAM_ERROR,message);
    }
    public GenericResponse error(int status, String message,Object data) {
        return ResponseFormat.error(status,message,data);
    }
    public GenericResponse success() {
        return ResponseFormat.success();
    }
    public GenericResponse success(Object data) {
        return ResponseFormat.success(data);
    }
    public GenericResponse success(Object data,String message) {
        return ResponseFormat.success(data);
    }


    public void responsePrint(HttpServletResponse response,String msg){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取合法ip地址
     * @return 返回ip
     */
    public String getRealIpAddress(HttpServletRequest request ) {
        String ip = request.getHeader("x-forwarded-for");//squid 服务代理
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");//apache服务代理
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");//weblogic 代理
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");//有些代理
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP"); //nginx代理
        }

        /*
         * 如果此时还是获取不到ip地址，那么最后就使用request.getRemoteAddr()来获取
         * */
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(StringUtils.equals(ip,LOCAL_IP) || StringUtils.equals(ip,DEFAULT_IP)){
                //根据网卡取本机配置的IP
                try {
                    ip= InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    logger.error("InetAddress getLocalHost error In HttpUtils getRealIpAddress: " ,e);
                }

            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        //"***.***.***.***".length() = 15
        if(!StringUtils.isEmpty(ip) && ip.length()> DEFAULT_IP_LENGTH){
            if(ip.indexOf(",") > 0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }
    private static final String LOCAL_IP = "127.0.0.1";//本地ip地址
    private static final String DEFAULT_IP = "0:0:0:0:0:0:0:1";//默认ip地址
    private static final int DEFAULT_IP_LENGTH = 15;//默认ip地址长度


}
