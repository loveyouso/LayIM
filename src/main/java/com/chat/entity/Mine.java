package com.chat.entity;

import lombok.Data;

/**
 * 我的消息
 * @author loveyouso
 * @create 2018-09-01 16:45
 **/
@Data
public class Mine {
    private int id;             //我的id
    private String username;    //我的昵称
    private Boolean mine;       //是否我发的信息
    private String avatar;      //我的头像
    private String content;     //消息内容
}
