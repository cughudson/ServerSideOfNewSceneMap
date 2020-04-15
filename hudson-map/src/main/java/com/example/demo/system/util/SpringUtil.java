package com.example.demo.system.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  public static void setContext(ApplicationContext applicationContext) {
    if (SpringUtil.applicationContext == null) {
      SpringUtil.applicationContext = applicationContext;
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (SpringUtil.applicationContext == null) {
      SpringUtil.applicationContext = applicationContext;
    }

    System.out.println("---------------------------------------------------------------------");
    System.out.println("------------------------util.SpringUtil------------------------------");
    System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext=" + SpringUtil.applicationContext + "========");
    System.out.println("---------------------------------------------------------------------");
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }
}
