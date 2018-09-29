package com.chat.entity;

import lombok.Data;

/**
 * 客户端接收消息类型
 * @author loveyouso
 * @create 2018-09-01 16:48
 **/
@Data
public class Receive {
    private Integer toId;           //发送给那个用户
    private Integer id;             //消息的来源（如果是私聊则是用户id，如果是群聊，则是群组id）
    private String username;        //
    private String avatar;          //
    private String Type;            //聊天窗口来源类型，从发送消息传递的to里面获取
    private String content;         //消息内容
    private int cid;                //消息id
    private Boolean mine;           //是否是我发的信息，为true则显示在右边
    private Integer fromId;         //消息的发送者id（比如群组中的某个消息发送者，用于解决多窗口问题）
    private Long timeStamp;         //服务器动态时间戳
    private int status;             //消息的状态
}
