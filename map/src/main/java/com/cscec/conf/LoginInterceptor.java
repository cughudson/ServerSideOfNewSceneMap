package com.cscec.conf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.cscec.controller.LoginController;
import com.cscec.model.ext.UserExt;
import com.cscec.util.response.ErrorCode;
import com.cscec.util.Constant;
import com.cscec.util.SpringUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

	private List<String> url = Lists.newArrayList();

	private Logger logger= LoggerFactory.getLogger(this.getClass());
	/**
	 * 开始进入地址请求拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info(request.getRequestURI()+"\t"+request.getRequestURL());
		HttpSession session = request.getSession();
		UserExt user= (UserExt) session.getAttribute(Constant.user);
		int time=request.getSession().getMaxInactiveInterval();
//		logger.info(time+":loginCheck:::"+request.getSession().getId());
		boolean otherLogin=false;
		if( user!= null){
			UserExt  active=Constant.userRandomMap.get(user.getId());
			 if(active!=null &&  !active.getRandomId().equals(user.getRandomId())){//判断是否在其他地方登陆  判断条件( 当前时间-最后登陆时间 < session 存活时间  并且两个randomId 不同)
				  //调用注销方法  然后重新进入该方法
				 SpringUtil.getBean(LoginController.class).logout(request);
//				 request.setAttribute("msg","其它地方登陆");
//				 return preHandle(request,response,handler);
				 otherLogin=true;
			 }else {
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
	/**
	 * 处理请求完成后视图渲染之前的处理操作
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView)   {
//		logger.info("postHandle");
	}

	/**
	 * 视图渲染之后的操作
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		 {
//		logger.info("afterCompletion");
	}
	
	/**
	 * 定义排除拦截URL
	 */
	public List<String> getUrl(){
		url.add("/login");      //登录页
        url.add("/logout");   //登录action URL
        //网站静态资源
        url.add("/css/**");
        url.add("/js/**");
        url.add("/lib/**");
        url.add("/fonts/**");
		url.add("/icon/**");
		url.add("/software/**");
		url.add("/images/**");
		url.add("/portrait/**");
		url.add("/img/**");
        url.add("/favicon.ico");
		url.add("/webjars/**");
		url.add("/swagger-resources/**");
		url.add("/swagger-ui.html");

		url.add("/configuration/**");
		url.add("/statics/**");
		url.add("/site.webmanifest");
		return url;
	}	
	
	 
}
