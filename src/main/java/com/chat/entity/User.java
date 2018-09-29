package com.chat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author loveyouso
 * @create 2018-09-01 17:44
 **/
@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String sign;
    private String avatar;
    private String email;
    private Date createTime;
    private Integer sex;
    private String Status;
    private String active;          //激活码
}
