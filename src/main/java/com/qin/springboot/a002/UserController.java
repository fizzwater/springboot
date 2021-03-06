package com.qin.springboot.a002;

import com.qin.springboot.dao.UserDao;
import com.qin.springboot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * //@EnableAutoConfiguration 加上后可以不让userDao，提示错误
 * 但是违背了本意
 * 可在File -- Settings -- Inspections。在Spring Core -- Autowring for Bean Class 中，
 * 将Severity的级别由之前的error改成warning。
 */
@Slf4j
@RestController
@RequestMapping("/test002")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("getuser")
    public User getUser() {
        //RoutingDataSource.setThreadLocalDataSourceKey(DataSourceType.readDataSource02.name());
        log.info("begin getUser ");
        User user = userDao.selectByPrimaryKey(524);
        return user;
    }


    @RequestMapping("adduser")
    public User addUser() {
        User user = new User();
        user.setName("test");
        user.setPwd("test");
        userDao.insert(user);
        return user;
    }


    @Transactional
    @RequestMapping("adduserTwice")
    public User[] adduserTwice() {
        User user2 = new User();
        user2.setName("test5");
        user2.setPwd("test5");
        userDao.insert(user2);

        User user1 = new User();
        user1.setName("test1");
        user1.setPwd("test1");
        userDao.insert(user1);
        return new User[]{user2,user1};
    }


}
