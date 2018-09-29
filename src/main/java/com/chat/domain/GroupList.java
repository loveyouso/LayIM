package com.chat.domain;

import lombok.Data;

/**
 * @author loveyouso
 * @create 2018-09-10 22:51
 * @description 好友分组列表
 **/
@Data
public class GroupList extends Group {

    //群头像地址
    private String avatar;
    //创建者id
    private Integer creatId;

    public GroupList(){
        super(null,null);
    }

    public GroupList(Integer id, String groupName) {
        super(id, groupName);
    }

    public GroupList(Integer id, String groupName, String avatar) {
        super(id, groupName);
        this.avatar = avatar;
    }
}