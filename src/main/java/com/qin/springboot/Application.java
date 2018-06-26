package com.qin.springboot;

import com.qin.springboot.a001.servlet.MyServlet;
import com.qin.springboot.a002.LoadAdditionalPropertiesListener;
import com.qin.springboot.a005.StatusEndpoint;
import com.qin.springboot.a005.StatusHealth;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 * @RestController = @Controller + @ResponseBody
 * @SpringBootApplication = @Configration +  @EnableAutoConfiguration + @ComponentScan
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
@ServletComponentScan
@MapperScan("com.qin.springboot.dao")
//@PropertySource({"classpath:jdbc.yml"})
@PropertySource({"classpath:application.yml"})
public class Application  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.listeners(new LoadAdditionalPropertiesListener()).sources(Application.class);
    }

    public static void main( String[] args ) {
        // 启动时加上environment监听
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners(new LoadAdditionalPropertiesListener());
        app.run(args);

       // SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean MyServlet() {
        return new ServletRegistrationBean(new MyServlet(), "/myServlet/*");

    }

    @Bean
    public Endpoint<String> status(){
        Endpoint<String> status = new StatusEndpoint();
        return status;
    }

    @Bean
    public StatusHealth statusHealth(){
        return new StatusHealth();
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }


}
