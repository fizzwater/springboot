package com.qin.springboot.a002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


import java.io.IOException;

//不要注册
//@Component
public class LoadAdditionalPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private ResourceLoader loader = new DefaultResourceLoader();

    //不同通过这种的方式获取
    //@Autowired
    //private Environment environment;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        try {

            System.out.println("add property 被创建");
            //properties文件
            Resource resource = loader.getResource("classpath:profiles/"+environment.getProperty("spring.profiles.active")+"/a.properties");
            PropertySource<?> propertySource = new PropertySourcesLoader().load(resource);
            environment.getPropertySources().addLast(propertySource);

            //yml文件
            /*resource = loader.getResource("classpath:profiles/"+environment.getProperty("spring.profiles.active")+"/test.yml");
            propertySource = new PropertySourcesLoader().load(resource);
            environment.getPropertySources().addLast(propertySource);*/


            resource = loader.getResource("classpath:profiles/"+environment.getProperty("spring.profiles.active")+"/test.yml");
            PropertySource<?> yamlTestProperties = new YamlPropertySourceLoader().load("yamlTestProperties", resource, null);
            environment.getPropertySources().addFirst(yamlTestProperties);

        }catch(IOException e){
            throw new IllegalStateException(e);
        }



    }
}