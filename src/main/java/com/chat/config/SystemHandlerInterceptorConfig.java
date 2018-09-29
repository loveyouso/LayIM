package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author loveyouso
 * @create 2018-09-15 11:56
 **/
@Configuration
public class SystemHandlerInterceptorConfig implements HandlerInterceptor {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * @description: 前置处理器，在请求处理之前调用
     * @param request
     * @param response
     * @param o
     * @return: boolean
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        LOGGER.info("前置处理器，在请求处理之前调用");
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/");
            //返回为false时，表示请求结束
            return false;
        } else {
            //当返回值为true 时就会继续调用下一个Interceptor 的preHandle 方法
            return true;
        }
    }

    /** 
     * @description: 请求处理之后进行调用，但是在视图被渲染之前(Controller方法调用之后)
     * @param request	
     * @param response	
     * @param o	
     * @param modelAndView	 
     * @return: void
    */ 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, 
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * @description: 后置处理器,请求处理之后进行调用，但是在视图被渲染之前(Controller方法调用之后)
     * @param request
     * @param response
     * @param o
     * @param e
     * @return: void
    */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object o, Exception e) throws Exception {

    }
}
