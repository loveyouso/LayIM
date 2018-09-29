package com.chat.domain;

import com.chat.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @author loveyouso
 * @create 2018-09-03 20:59
 **/
@Data
public class FriendList extends Group {

    private List<User> list;

    public FriendList(Integer id, String groupName) {
        super(id, groupName);
    }

    public FriendList(Integer id, String groupName, List<User> list) {
        super(id, groupName);
        this.list = list;
    }

    public FriendList(List<User> list){
        super(null,null);
        this.list = list;
    }
}
