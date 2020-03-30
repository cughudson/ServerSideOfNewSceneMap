package com.example.demo;

import com.example.demo.system.config.CustomMultipartResolver;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
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

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        springApplication.addListeners((ApplicationListener<ApplicationStartedEvent>) DemoApplication::onApplicationEvent);
        springApplication.addListeners((ApplicationListener<ApplicationReadyEvent>) DemoApplication::onApplicationReadyEvent);
        springApplication.run(args);
    }
    private static void onApplicationEvent(ApplicationStartedEvent event) {
//        SystemCommonService service= SpringUtil.getBean(SystemCommonServiceImpl.class);
//        SystemConfig front= service.selectByCode(Constant.PASS_URLS);
    }
    private static void onApplicationReadyEvent(ApplicationReadyEvent event) {

    }
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver()
    {
        return new CustomMultipartResolver();
    }

}

