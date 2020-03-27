package com.cscec.conf;

import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties.WebStatFilter;
@Configuration
/**
 * sql 监控必须要有这个
 * @author Administrator
 *
 */
public class MyWebStatFilter   extends WebStatFilter {
}
