package com.chat.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.beans.Customizer;

/**
 * @author loveyouso
 * @create 2018-09-14 8:46
 **/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    //定义授权规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.csrf().disable();
    }

    //定义认证规则
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("zhangsan").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1")
                .and()
                .withUser("lisi").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2")
                .and()
                .withUser("wnagwu").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip3");
    }
}
