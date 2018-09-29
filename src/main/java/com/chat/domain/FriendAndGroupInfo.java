package com.chat.domain;

import com.chat.entity.User;
import lombok.Data;

import java.util.List;

/**
 *
 * @author loveyouso
 * @create 2018-09-03 20:57
 **/
@Data
public class FriendAndGroupInfo {

    private User mine;

    private List<FriendList> friend;

    private List<GroupList> group;

}
