//package com.hl.experiment.security.xss;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class XssMvcConfig implements WebMvcConfigurer {
//
//    private static final Logger logger = LoggerFactory.getLogger(XssMvcConfig.class);
//
//    @Autowired
//    private XssInterceptor xssInterceptor;
//
//    @Autowired
//    private XssConfig xssConfig;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        //注册拦截器并配置拦截的路径
//        String[] interceptUrls = xssConfig.getUrlPatterns().split(",");
//        String[] exceptUrls = xssConfig.getExcludes().split(",");
//        registry.addInterceptor(xssInterceptor)
//                .addPathPatterns(interceptUrls)
//                .excludePathPatterns(exceptUrls);
//
//        logger.info("Registered Spring Mvc Interceptor: {}", xssInterceptor);
//    }
//}