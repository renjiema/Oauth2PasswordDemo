package com.mrj.oauthdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author MRJ
 * @date 2019/9/30 10:12
 * @Description: 配置WebSecurity
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启方法级的注解
//*****************************************************//
//@PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
//@PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
//@PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
//@PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
//*****************************************************//
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  允许所有人访问 '/oauth' 以下的目录
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable()      //关闭  csrf
                .authorizeRequests()
                .antMatchers("/**").authenticated()    //其他目录需要认证
                .and()
                .httpBasic();                          //开启基本http验证
    }
    // 把 PasswordEncoder 放到  Spring 容器中
    // Springboot2 貌似必须把这个配置到 Spring 容器中，不然会报错
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //把 AuthenticationManager 配置到 Spring 容器中，配置Oauth2 的时候会用到
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
