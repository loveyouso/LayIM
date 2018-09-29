package com.chat.domain;

import com.chat.common.SystemConstant;
import lombok.Data;

/**
 * @author loveyouso
 * @create 2018-09-10 22:57
 **/
@Data
public class ResultSet<T> {

    //状态：0表示成功，其他表示失败
    private int code = SystemConstant.SUCCESS;
    //额外的信息
    private String msg = SystemConstant.SUCCESS_MESSAGE;

    //携带的数据
    private T data;

    public ResultSet() {
    }

    public ResultSet(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultSet(T data) {
        this.data = data;
    }
}
