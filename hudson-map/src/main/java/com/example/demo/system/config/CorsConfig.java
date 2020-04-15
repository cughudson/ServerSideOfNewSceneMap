package com.example.demo.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author zhangxg
 * @since 2020-03-31 23:05:31
 */

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    // 4
    source.registerCorsConfiguration("/**", buildConfig());
    return new CorsFilter(source);
  }

  private CorsConfiguration buildConfig() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    // 1 允许任何域名使用
    corsConfiguration.addAllowedOrigin("*");
    // 2 允许任何头
    corsConfiguration.addAllowedHeader("*");
    // 3 允许任何方法（post、get等）
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true);
    return corsConfiguration;
  }

}


