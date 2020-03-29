package com.cscec;

import com.cscec.conf.fileupload.CustomMultipartResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
    @Bean
    public CookieSerializer httpSessionIdResolver() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 取消仅限同一站点设置
        cookieSerializer.setSameSite(null);
        cookieSerializer.setCookieName("mySessionId");
        return cookieSerializer;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2 允许任何头
        corsConfiguration.addAllowedMethod("*");// 3 允许任何方法（post、get等）
//        corsConfiguration.allowCredentials(false)
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
