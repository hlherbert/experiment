package com.hl.experiment.security;

import com.hl.experiment.security.xss.XssConfig;
import com.hl.experiment.security.xss.XssInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityMvcConfig implements WebMvcConfigurer {

    private XssInterceptor xssInterceptor;

    @Autowired
    private XssConfig xssConfig;

    @Bean
    public XssInterceptor getXssInterceptor() {
        return new XssInterceptor(xssConfig);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器并配置拦截的路径
        registry.addInterceptor(xssInterceptor)
                .addPathPatterns(xssConfig.getUrlPatterns().split(","))
                .excludePathPatterns(xssConfig.getExcludes().split(","));
    }
}