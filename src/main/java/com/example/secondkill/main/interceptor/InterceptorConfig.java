package com.example.secondkill.main.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
       public void addInterceptors(InterceptorRegistry registry) {
            //注册TestInterceptor拦截器
            InterceptorRegistration registration = registry.addInterceptor(new MyInterceptor());
            registration.addPathPatterns("/order/test1");                      //所有路径都被拦截
            registration.excludePathPatterns(                         //添加不拦截路径
            );
        }

}
