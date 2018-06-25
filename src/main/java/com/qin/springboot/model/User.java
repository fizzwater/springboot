package com.qin.springboot.model;

import lombok.Data;

/**
 *  user

 CREATE TABLE USER
 (
 id INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(50) NOT NULL,
 pwd VARCHAR(100) ,
 PRIMARY KEY (id)
 )
 ENGINE=InnoDB DEFAULT CHARSET=utf8;

 */
@Data
public class User {

    private int id;

    private String name;

    private String pwd;

}
