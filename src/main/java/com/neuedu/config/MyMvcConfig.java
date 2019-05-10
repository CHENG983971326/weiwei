package com.neuedu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:E:/file/");
    }


    @Bean
    public SecurityInterceptor getSecurityInterceptor(){
        return  new SecurityInterceptor();
    }

    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        //默认到登陆页
        registry.addViewController("/").setViewName("forward:/user/login");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        //排除配置
        addInterceptor.excludePathPatterns("/user/login");
        addInterceptor.excludePathPatterns("/user/login.do");
        addInterceptor.excludePathPatterns("/user/registerpage");
        addInterceptor.excludePathPatterns("/user/register");
        addInterceptor.excludePathPatterns("/static/js/**");
        addInterceptor.excludePathPatterns("/static/css/**");
        addInterceptor.excludePathPatterns("/static/img/**");
        addInterceptor.excludePathPatterns("/static/assets/**");
        //拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession();
            //判断是否已有该用户登录的session
            if(session.getAttribute("loginuser") !=null){
                return true;
            }
            //跳转到登录页
            String url = "/user/login";
            response.sendRedirect(url);
            return false;
        }
    }
}


