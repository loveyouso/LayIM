package com.chat.domain;

import lombok.Data;

/**
 * @author loveyouso
 * @create 2018-09-10 22:56
 * 分页结果集
 **/
@Data
public class ResultPageSet extends ResultSet{

    private int pages;

    public ResultPageSet() {
        super();
    }

    public ResultPageSet(Object data, int pages) {
        super(data);
        this.pages = pages;
    }
}
