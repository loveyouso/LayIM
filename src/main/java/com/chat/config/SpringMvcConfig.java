package com.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author loveyouso
 * @create 2018-09-15 9:10
 **/
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * @description: 重写addViewControllers方法设置主页
     * @param registry
     * @return: void
    */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /** 
     * @description: 添加拦截器
     * @param registry	 
     * @return: void
    */ 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns用于添加拦截规则，excludePathPatterns用户排除拦截
        registry.addInterceptor(new SystemHandlerInterceptorConfig())
                .addPathPatterns("/**").excludePathPatterns("/").excludePathPatterns("/*.html")
                .excludePathPatterns("/user/login").excludePathPatterns("/user/register")
                .excludePathPatterns("/user/existEmail");
        super.addInterceptors(registry);
    }
}
