package com.chat.entity;

import lombok.Data;

import java.util.Date;

/**
 * 添加好友、群组信息
 * @author loveyouso
 * @create 2018-09-01 16:26
 **/
@Data
public class AddMessage {
    private Integer id;
    private Integer fromUid;
    private Integer toUid;
    private Integer groupId;    //若是添加好友则为from_id的分组id，若是群组则为群组id
    private String remark;      //附言
    private int agree;          //是否处理：0未处理、1同意、2拒绝
    private int type;           //类型，可能是添加好友或群组
    private Date time;          //申请时间
}
