package com.chat.websocket.domain;

import lombok.Data;

/**
 * @description 同意添加好友
 * @author loveyouso
 * @create 2018-09-20 11:24
 **/
@Data
public class AgreeAddGroup {

    private Integer toUid;

    private Integer groupId;

    private Integer messageBoxId;
}
