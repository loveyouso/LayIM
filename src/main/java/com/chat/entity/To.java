package com.chat.entity;

import lombok.Data;

/**
 * 对方的信息
 * @author loveyouso
 * @create 2018-09-01 16:56
 **/
@Data
public class To {
    private Integer id;             //对方的id
    private String username;
    private String sign;
    private String avatar;
    private String status;          //状态
    private String Type;            //聊天类型，一般分为friend和group，group即为群聊
}
