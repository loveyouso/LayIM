package com.chat.entity;

import lombok.Data;

/**
 * @author loveyouso
 * @create 2018-09-01 16:16
 **/
@Data
public class AddFriends {
    private int mind;   //自己的id
    private int mgid;   //分组的id
    private int tid;    //对方用户的id
    private int tgid;   //对方分组的id

    public AddFriends() {
    }

    public AddFriends(int mind, int mgid, int tid, int tgid) {
        this.mind = mind;
        this.mgid = mgid;
        this.tid = tid;
        this.tgid = tgid;
    }
}
