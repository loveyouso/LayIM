package com.chat.util;

import com.chat.common.SystemConstant;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @description 服务器文件工具
 * @author loveyouso
 * @create 2018-09-17 23:27
 **/
public class FileUtil {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private DateUtil dateUtil;
    private UUIDUtil uuidUtil;

    /**
     * @description: 文件保存服务器
     * @param types 文件类型
     * @param path 文件绝对路径地址
     * @param file 二进制文件
     * @return: java.lang.String 文件的相对路径地址
    */
    public String upload(String types, String path, MultipartFile file){
        //getOriginalFilename()获取上传文件的原名
        String name = file.getOriginalFilename();
        dateUtil = new DateUtil();
        String paths = path + types + dateUtil.getDateString() +"/";
        String result = types + dateUtil.getDateString() + "/";
        //如果是图片，则使用uuid重命名图片
        if(SystemConstant.IMAGE_PATH.equals(types)){
            name = uuidUtil.getUUID32String() + name.substring(name.indexOf("."));
        }else if (SystemConstant.FILE_PATH.equals(types)){
            //如果是文件，则区分目录
            String p = uuidUtil.getUUID32String();
            paths = paths + p;
            result += p + "/";
        }
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(paths,name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件上传出错");
        }
        return result + name;
    }

    /**
     * @description 头像
     * @param realpath 服务器绝对路径地址
     * @param file 文件
     * @return 相对路径
     */
    public String upload(String realpath,MultipartFile file){
        String name = file.getOriginalFilename();
        name = uuidUtil.getUUID32String() + name.substring(name.indexOf("."));
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(realpath,name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("上传头像文件出错");
        }
        return SystemConstant.AVATAR_PATH + name;
    }

}
