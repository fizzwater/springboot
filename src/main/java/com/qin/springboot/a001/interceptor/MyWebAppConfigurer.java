package com.qin.springboot.a001.interceptor;

import com.qin.springboot.a001.interceptor.MyInterceptor1;
import com.qin.springboot.a001.interceptor.MyInterceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {


    /**
     * 初始页面
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry ) {
        registry.addViewController("/").setViewName( "forward:/index.html" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers(registry);
    }


    /**
     * 自定义静态资源路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadImg/**").addResourceLocations("file:/data/share/plane_images/");
        super.addResourceHandlers(registry);
    }

    /**
     * 对于前后端分离的项目来说，如果前端项目与后端项目部署在两个不同的域下，
     * 那么势必会引起跨域问题的出现。
     * 跨域处理 映射所有路径 允许所有来源 以下方法请求
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH");
    }

//        @Override
//        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//            converters.add(new FastJsonHttpMessageConverter());
//            converters.add(new ByteArrayHttpMessageConverter());
//            super.configureMessageConverters(converters);
//        }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 多个拦截器组成一个拦截器链

        // addPathPatterns 用于添加拦截规则

        // excludePathPatterns 用户排除拦截

        registry.addInterceptor(new MyInterceptor1()).addPathPatterns("/**");

        registry.addInterceptor(new MyInterceptor2()).addPathPatterns("/**");

        super.addInterceptors(registry);

    }
}