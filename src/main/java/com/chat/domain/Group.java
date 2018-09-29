package com.chat.domain;

import lombok.Data;

/**
 * 组信息
 * @author loveyouso
 * @create 2018-09-03 21:02
 **/
@Data
public class Group {
    private Integer id;
    private String groupName;

    public Group(Integer id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }
}
