package com.chat.entity;

import lombok.Data;

/**
 * 添加好友、群组信息
 * @author loveyouso
 * @create 2018-09-01 16:14
 **/
@Data
public class Add {
    private Integer groupId;
    private String remark;
    private int Type;
}
