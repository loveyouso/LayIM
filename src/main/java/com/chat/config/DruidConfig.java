package com.chat.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author loveyouso
 * @create 2018-09-11 18:31
 * @Description 配置druid的监控
 **/
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    /**
    * @Description: druid配置访问路径和用户名密码
    * @Param: []
    * @return: org.springframework.boot.web.servlet.ServletRegistrationBean
    */
    public ServletRegistrationBean startViewSevlet(){
        ServletRegistrationBean druid = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> params = new HashMap<>();
        params.put("loginUsername","admin");
        params.put("loginPassword","123456");
        params.put("allow","");    //默认就是允许所有访问
        params.put("resetEnable","false");
        druid.setInitParameters(params);
        return druid;
    }

    //配置一个filter

    /** 
    * @Description: 拦截器配置
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean
    */ 
    public FilterRegistrationBean webStartFilter(){
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new WebStatFilter());
        Map<String,String> params = new HashMap<>();
        params.put("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filter.setInitParameters(params);
        filter.setUrlPatterns(Arrays.asList("/*"));
        return filter;
    }

}
