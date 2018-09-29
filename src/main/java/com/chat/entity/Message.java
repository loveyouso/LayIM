package com.chat.entity;

import lombok.Data;

/**
 * 消息
 * @author loveyouso
 * @create 2018-09-01 16:38
 **/
@Data
public class Message {
    private String Type;
    private Mine mine;       //我的消息
    private To to;           //对方的消息
    String msg;        //额外的信息
}
