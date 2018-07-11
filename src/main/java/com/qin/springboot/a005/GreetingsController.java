package com.qin.springboot.a005;

import com.qin.springboot.a002.MyWebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义 metrics
 */
@RestController
public class GreetingsController {

    @Autowired
    private CounterService counterService;

    @Autowired
    private MyWebConfig myWebConfig;


    @Autowired
    private Environment environment;

    @RequestMapping("/greet")
    public String greet(String name) {
        counterService.increment("myapp.greet.count");
        return "Hello!   "+name +"<br/>"+environment.getProperty("good.dd.val")+"<br/> "+environment.getProperty("spring.profiles.active")+"<br/>"+environment.getProperty("qinchao.encoding");
    }
}