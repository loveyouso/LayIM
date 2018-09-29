package com.chat.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author loveyouso
 * @create 2018-09-10 22:53
 **/
@Data
public class GroupNumber {

    //群组编号
    private Integer gid;
    //用户编号
    private Integer uid;

    public GroupNumber() {
    }

    public GroupNumber(Integer gid, Integer uid) {
        this.gid = gid;
        this.uid = uid;
    }

}
