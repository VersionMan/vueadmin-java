package com.markerhub.config;

import com.markerhub.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //加载安全策略
@EnableGlobalMethodSecurity(prePostEnabled = true) //请求之前进行权限校验
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    LoginFailureHandler loginFailureHandler;
//
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
//
    @Autowired
    CaptchaFilter captchaFilter;

    @Bean
     JwtAuthenticationFilter  jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =new JwtAuthenticationFilter(authenticationManager());
        return  jwtAuthenticationFilter;
    }
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailServiceImpl userDetailService;
    //jwt 退出
    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    //默认放行的路径
    private static final String[] URL_WHITELIST = {

            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",

    };
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable() //允许跨域 和 关闭 csrf
               // 登录配置
				.formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

                // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                //在进行用户账号密码认证之前 先 认证过滤器拦截
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

        ;
    }
    //注入自定义userdetail
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
