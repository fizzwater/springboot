package com.qin.springboot.a001.interceptor;

import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.qin.springboot.a001.interceptor.MyInterceptor1;
import com.qin.springboot.a001.interceptor.MyInterceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

    @Override
   public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter=new StringHttpMessageConverter(Charset.forName("UTF-8"));
        List<MediaType> stringMediaTypes = new ArrayList<>();
        stringMediaTypes.add(MediaType.TEXT_PLAIN);
        stringMediaTypes.add(MediaType.TEXT_HTML);
        stringMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        stringHttpMessageConverter.setSupportedMediaTypes(stringMediaTypes);
        converters.add(stringHttpMessageConverter);

        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
        //FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        //fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMediaTypes.add(MediaType.ALL);
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        //fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //5.将convert添加到converters当中.
        converters.add(fastJsonHttpMessageConverter);


        super.configureMessageConverters(converters);

        System.out.println(converters);
    }

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