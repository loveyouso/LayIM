package com.chat.util;

import java.util.UUID;

/**
 * @description UUID工具
 * @author loveyouso
 * @create 2018-09-18 13:38
 **/
public class UUIDUtil {
    /**
     * @description 64位随机UUID
     * @return String
     */
    public String getUUID64String(){
        return (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replaceAll("-","");
    }
    /**
     * @description 32位随机UUID
     * @return String
     */
    public String getUUID32String(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
