package com.chat.entity;

import lombok.Data;

/**
 * 聊天记录返回信息
 * @author loveyouso
 * @create 2018-09-01 16:33
 **/
@Data
public class ChatHistory {
    private Integer id;            //用户id
    private String username;
    private String avatar;
    private String content;
    private Long timestamp;        //时间戳

    public ChatHistory() {
    }

    public ChatHistory(Integer id, String username, String avatar, String content, Long timestamp) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.content = content;
        this.timestamp = timestamp;
    }
}
