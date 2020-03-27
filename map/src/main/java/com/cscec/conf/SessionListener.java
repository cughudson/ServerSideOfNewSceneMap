package com.cscec.conf;

import com.cscec.model.ext.UserExt;
import com.cscec.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
//        logger.info(LocalDateTime.now()+"---sessionCreated----"+event.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException {
//        logger.info(LocalDateTime.now()+"---sessionDestroyed----");
        HttpSession session = event.getSession();
        logger.info("deletedSessionId: " + session.getId());
        UserExt user= (UserExt) session.getAttribute(Constant.user);
        if(user!=null){// 登录过的用户
            Constant.userRandomMap.remove(user.getId());
        }
    }

}