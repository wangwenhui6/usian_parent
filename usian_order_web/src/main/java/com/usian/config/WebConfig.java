package com.usian.config;

import com.usian.interceptor.UserLoginInterception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserLoginInterception userLoginInterception;

    /**
     * 注册拦截器 （springmvc）
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(userLoginInterception);
        //拦截的uri
        registration.addPathPatterns("/frontend/order/**");
    }
}
