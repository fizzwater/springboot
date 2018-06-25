package com.qin.springboot.dao;

import com.qin.springboot.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface  UserDao {

   /* @Select("select * from user where id=#{arg1}")
    @Results({
            @Result(property = "userName", column = "user_name")
    })*/
    User selectByPrimaryKey(Integer id);

    void insert(User user);
}
