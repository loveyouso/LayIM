package com.chat.entity;

import lombok.Data;

/**
 * @author loveyouso
 * @create 2018-09-01 16:36
 **/
@Data
public class FriendGroup {
    private Integer uid;            //用户id
    private String groupName;       //群组名称

    public FriendGroup() {
    }

    public FriendGroup(Integer uid, String groupName) {
        this.uid = uid;
        this.groupName = groupName;
    }
}
