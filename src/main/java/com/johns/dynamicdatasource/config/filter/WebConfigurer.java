//package com.johns.dynamicdatasource.config.filter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * 注册拦截器
// *
// * @author johns-li
// * @date 2021/06/23
// */
//@Configuration
//public class WebConfigurer implements WebMvcConfigurer {
//
//    @Autowired
//    private LoginInterceptor loginHandlerInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration ir = registry.addInterceptor(loginHandlerInterceptor);
//        // 拦截路径
//        ir.addPathPatterns("/*");
//        // 不拦截路径
//        List<String> irs = new ArrayList<String>();
//        irs.add("/login");
//        irs.add("/api/*");
//        // 开放knife4j
//        irs.add("/doc.html");
//        irs.add("/service-worker.js");
//        irs.add("/swagger-resources");
//        ir.excludePathPatterns(irs);
//    }
//}