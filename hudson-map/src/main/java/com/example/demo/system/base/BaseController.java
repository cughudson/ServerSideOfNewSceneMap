package com.example.demo.system.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.entity.ext.UserExt;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.response.GenericResponse;
import com.example.demo.system.response.ResponseFormat;
import com.example.demo.system.service.CommonService;
import com.example.demo.system.service.UserService;
import com.example.demo.system.util.Constant;
import com.github.pagehelper.util.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class BaseController {
  public Logger logger = LoggerFactory.getLogger(this.getClass());

  public DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  public ExecutorService cachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
          60L, TimeUnit.SECONDS,
          new SynchronousQueue<Runnable>());

  //            Executors.newCachedThreadPool();

  /**
   * 从thread local获取网络上下文
   *
   * @return
   */
  public HttpServletRequest getServletRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes servletRequestAttributes;
    if (requestAttributes instanceof ServletRequestAttributes) {
      servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
      return servletRequestAttributes.getRequest();
    }
    return null;
  }

  /**
   * 获取当前客户端session对象
   *
   * @return
   */
  public HttpSession getSession() {
    return getServletRequest().getSession();
  }


  public JSONObject getJson() {
    return new JSONObject();
  }

  public Map<String, Object> paramMapToHashMap(Map<String, String[]> parameterMap) {
    Map<String, Object> result = new HashMap<>(parameterMap.size());
    for (String key : parameterMap.keySet()) {
      if (parameterMap.get(key).length == 1) {
        result.put(key, parameterMap.get(key)[0]);
      } else if (parameterMap.get(key).length > 0) {
        result.put(key, parameterMap.get(key));
      }
    }
    result.remove(Constant.pageNum);
    result.remove(Constant.pageSize);
    return result;
  }

  public GenericResponse error(int status, String message) {
    return ResponseFormat.error(status, message);
  }

  public GenericResponse error(String message) {
    return ResponseFormat.error(ErrorCode.PARAM_ERROR, message);
  }

  public GenericResponse error(int status, String message, Object data) {
    return ResponseFormat.error(status, message, data);
  }

  public GenericResponse success() {
    return ResponseFormat.success();
  }

  public GenericResponse success(Object data) {
    return ResponseFormat.success(data);
  }

  public GenericResponse success(Object data, String message) {
    return ResponseFormat.success(data);
  }

  public GenericResponse success(Object data, Long size) {
    return ResponseFormat.success(data, size);
  }


  public void responsePrint(HttpServletResponse response, String msg) {
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
   *
   * @return 返回ip
   */
  public String getRealIpAddress(HttpServletRequest request) {
    String unknown = Constant.unknown;
    String ip = request.getHeader("x-forwarded-for"); //squid 服务代理
    if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");//apache服务代理
    }
    if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");//weblogic 代理
    }

    if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");//有些代理
    }

    if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Real-IP"); //nginx代理
    }

    /*
     * 如果此时还是获取不到ip地址，那么最后就使用request.getRemoteAddr()来获取
     * */
    if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
      if (LOCAL_IP.equals(ip) || DEFAULT_IP.equals(ip)) {
        //根据网卡取本机配置的IP
        try {
          ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
          logger.error("InetAddress getLocalHost error In HttpUtils getRealIpAddress: ", e);
        }

      }
    }
    //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    //"***.***.***.***".length() = 15
    if (StringUtil.isNotEmpty(ip) && ip.length() > DEFAULT_IP_LENGTH) {
      if (ip.indexOf(",") > 0) {
        ip = ip.substring(0, ip.indexOf(","));
      }
    }
    return ip;
  }

  private static final String LOCAL_IP = "127.0.0.1";//本地ip地址
  private static final String DEFAULT_IP = "0:0:0:0:0:0:0:1";//默认ip地址
  private static final int DEFAULT_IP_LENGTH = 15;//默认ip地址长度


  @Autowired
  public HttpServletRequest request;
  @Autowired
  public CommonService commonService;
  @Autowired
  public UserService userService;
  @Value("${img-save-path}")
  public String savePath;

  public String imgFolder = "/image_folder";

  public String like(String name) {
    return "%" + name + "%";
  }

  public UserExt getUser() {
    return (UserExt) request.getSession().getAttribute(Constant.user);
  }

  public JSONObject getOutUser() {
    UserExt user = (UserExt) request.getSession().getAttribute(Constant.user);
    return JSON.parseObject(JSON.toJSONString(user)).fluentRemove(Constant.password);
  }

  public JSONObject getLoginRecord(String ip, Long userId, int type, String result, String username) {
    JSONObject json = new JSONObject();
    json.put(Constant.ip, ip);
    json.put(Constant.userId, userId);
    json.put(Constant.type, type);
    json.put(Constant.result, result);
    json.put(Constant.username, username);
    json.put(Constant.loginTime, new Date());
    return json;
  }

}

