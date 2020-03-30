package com.example.demo.system.config;


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {
    private static final Logger log = LoggerFactory.getLogger(SessionListener.class);

    public SessionListener() {
    }

    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    public void sessionCreated(HttpSessionEvent event) {
    }

    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException {
        HttpSession session = event.getSession();
        log.info("deletedSessionId: " + session.getId());
    }
}
