package com.cscec;

import com.cscec.conf.fileupload.CustomMultipartResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.cscec.mapper"})
@SpringBootApplication
@EnableAsync
@EnableSwagger2
@EnableScheduling
public class CscecApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CscecApplication.class);
        springApplication.addListeners((ApplicationListener<ApplicationStartedEvent>) CscecApplication::onApplicationEvent);
        springApplication.run(args);
    }

    private static void onApplicationEvent(ApplicationStartedEvent event) {
//        SpringUtil.getBean(Scheduler.class).init();
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        return new CustomMultipartResolver();
    }

}
