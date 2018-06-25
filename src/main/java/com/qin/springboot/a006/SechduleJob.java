package com.qin.springboot.a006;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *定时任务
 */
@Component
public class SechduleJob {
    private static final Logger logger = LoggerFactory.getLogger(SechduleJob.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final String dsUrlOne = "http://localhost:8081/hellox?name=kitty";
    private static final String dsUrlTwo = "http://localhost:8081/hello/";

    //@Scheduled(fixedRate = 5000)
    public void helloJob(){
        System.out.println("5s===="+dateFormat.format(new Date()));
    }

   // @Scheduled(fixedRate = 3000)
    public void helloxJob(){
        System.out.println("3s===="+dateFormat.format(new Date()));
    }
}
