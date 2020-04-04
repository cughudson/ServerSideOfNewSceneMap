package com.example.demo;

import com.example.demo.system.config.CustomMultipartResolver;
import com.example.demo.system.util.SpringUtil;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.multipart.MultipartResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@MapperScan(basePackages = {"com.example.demo.*.mapper","com.example.demo.mapper"})
@SpringBootApplication
@Controller
@EnableSwagger2
public class DemoApplication {

//    SpringBoot Application共支持6种事件监听，按顺序分别是：
//
//    1 ApplicationStartingEvent：在Spring最开始启动的时候触发
//    2 ApplicationEnvironmentPreparedEvent：在Spring已经准备好上下文但是上下文尚未创建的时候触发
//    3 ApplicationPreparedEvent：在Bean定义加载之后、刷新上下文之前触发
//    4 ApplicationStartedEvent：在刷新上下文之后、调用application命令之前触发
//    5 ApplicationReadyEvent：在调用applicaiton命令之后触发
//    6 ApplicationFailedEvent：在启动Spring发生异常时触发
//
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        springApplication.addListeners((ApplicationListener<ApplicationStartedEvent>) DemoApplication::onApplicationEvent);

        springApplication.addListeners((ApplicationListener<ApplicationPreparedEvent>) event -> {
            System.out.println("application prepared event");
        });

        springApplication.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
            System.out.println("application ready event");
        });

        ApplicationContext applicationContext = springApplication.run(args);
//        SpringUtil.setContext(applicationContext);
    }
    private static void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("application Started event");
//        SystemCommonService service= SpringUtil.getBean(SystemCommonServiceImpl.class);
//        SystemConfig front= service.selectByCode(Constant.PASS_URLS);
    }
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        return new CustomMultipartResolver();
    }

//    @Bean
//    public DefaultCookieSerializer httpSessionIdResolver() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setSameSite("NONE");// 取消仅限同一站点设置
//        cookieSerializer.setUseHttpOnlyCookie(false);
////        cookieSerializer.setUseSecureCookie(true);
//        cookieSerializer.setCookieName("jsid");
//        return cookieSerializer;
//    }
}

