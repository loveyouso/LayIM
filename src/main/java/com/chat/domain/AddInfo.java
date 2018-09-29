package com.chat.domain;

import com.chat.entity.User;
import lombok.Data;

/**
 * 返回添加好友信息、群组信息
 * @author loveyouso
 * @create 2018-09-03 20:37
 **/
@Data
public class AddInfo {

    private Integer id;        //
    private Integer uid;       //
    private String content;
    private int from;
    private int from_group;
    private int Type;
    private String remark;
    private String href;
    private Integer read;
    private String time;
    private User user;

    @Override
    public String toString() {
        return "AddInfo{" +
                "id=" + id +
                ", uid=" + uid +
                ", content='" + content + '\'' +
                ", from=" + from +
                ", from_group=" + from_group +
                ", type=" + Type +
                ", remark='" + remark + '\'' +
                ", href='" + href + '\'' +
                ", read=" + read +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
