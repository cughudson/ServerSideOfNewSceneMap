package com.example.demo.system.config;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.controller.LoginController;
import com.example.demo.system.entity.ext.UserExt;
import com.example.demo.system.response.ErrorCode;
import com.example.demo.system.util.Constant;
import com.example.demo.system.util.SpringUtil;
import com.google.common.collect.Lists;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static List<String> url = Lists.newArrayList();

    public LoginInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        if(getUrl().contains(request.getRequestURI())){
//            return true;
//        }

        logger.info(request.getRequestURI()+"\t"+request.getRequestURL());
        HttpSession session = request.getSession();
        UserExt user= (UserExt) session.getAttribute(Constant.user);
//        int time=request.getSession().getMaxInactiveInterval();
//		logger.info(time+":loginCheck:::"+request.getSession().getId());
        boolean otherLogin=false;
        if( user!= null){
            UserExt  active= Constant.userRandomMap.get(user.getId());
            if(active!=null &&  !active.getRandomId().equals(user.getRandomId())){//判断是否在其他地方登陆  判断条件( 当前时间-最后登陆时间 < session 存活时间  并且两个randomId 不同)
                //调用注销方法  然后重新进入该方法
                SpringUtil.getBean(LoginController.class).logout(request);
//				 request.setAttribute("msg","其它地方登陆");
//				 return preHandle(request,response,handler);
                otherLogin=true;
            }else {
                if("/".equals(request.getRequestURI())){
                    response.sendRedirect("/login");	//未登录，跳转到登录页
                    return false;
                }
                return true;//暂时不管权限
            }
//			return checkPower(request,response,user);
        }
        if("GET".equals(request.getMethod().toUpperCase())){
//				if(request.getAttribute("msg")!=null){
//					session.setAttribute("message",request.getAttribute("msg"));
//				}else{
//					session.setAttribute("message","请先登录")	;
//				}
            if(otherLogin){
                response.sendRedirect("/login?otherlogin");//其它地方登录
            }else{
                response.sendRedirect("/login");	//未登录，跳转到登录页
            }
        }else{
            JSONObject json=new JSONObject();
            json.put(Constant.code, ErrorCode.USER_PLEASE_LOGIN);
            if(otherLogin){
                json.put(Constant.message,"其它地方登陆");
            }else{
                json.put(Constant.message,"请先登录");
            }
            //设置缓存区编码为UTF-8编码格式
            response.setCharacterEncoding("UTF-8");
            //在响应中主动告诉浏览器使用UTF-8编码格式来接收数据
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            //可以使用封装类简写Content-Type，使用该方法则无需使用setCharacterEncoding
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(json.toString());
        }
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        String requestId=request.getHeader("id");
//        logger.info("postHandle:" + request.getRequestURL() );
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
//        logger.info("afterCompletion:" + request.getRequestURL() );
    }

    public void initUrl() {
        url.add("/login");
        url.add("/css/**");
        url.add("/static/js/**");
        url.add("/js/**");
        url.add("/image_folder/**");
        url.add("/lib/**");
        url.add("/fonts/**");
        url.add("/icon/**");
        url.add("/images/**");
        url.add("/portrait/**");
        url.add("/img/**");
        url.add("/favicon.ico");
        url.add("/webjars/**");
        url.add("/swagger-resources/**");
        url.add("/configuration/**");
        url.add("/statics/**");
        url.add("/site.webmanifest");
        url.add("/swagger-ui.html");
        url.add("/api/**");
        url.add("/page/**");
        url.add("/admin/**");
    }

    public List<String> getUrl() {
        if (url.size() == 0) {
            this.initUrl();
//            SystemConfig front = ((SystemCommonServiceImpl)SpringUtil.getBean(SystemCommonServiceImpl.class)).selectByCode("PASS_URLS");
//            url.addAll(Arrays.asList(front.getValue().split(";")));
        }

        return url;
    }

//    public void reloadUrl() {
//        url.clear();
//        this.getUrl();
//    }
}