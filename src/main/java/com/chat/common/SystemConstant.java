package com.chat.common;

import lombok.val;

/**
 * @author loveyouso
 * @create 2018-09-10 23:05
 **/
public class SystemConstant {

   public final static int SUCCESS = 0;

   public final static int ERROR = 1;

   public final static String LOGGIN_SUCCESS = "登陆成功";

   public final static String LOGGIN_FAIL = "登陆失败";

   public final static String NONACTIVED = "用户未激活";

   public final static String REGISTER_SUCCESS = "注册成功";

   public final static String REGISTER_FAIL = "登陆失败";

   public final static String SUCCESS_MESSAGE = "操作成功";

   public final static String ERROR_MESSAGE = "操作失败";

   public final static String UPLOAD_SUCCESS = "上传成功";

   public final static String UPLOAD_FAIL = "上传失败";

   public final static String IMAGE_PATH = "/upload/image/";

   public final static String FILE_PATH = "/upload/file/";

   public final static String AVATAR_PATH = "/static/image/avatar/";

   public final static String GROUP_AVATAR_PATH = "/static/image/group/";

   public final static String DEFAULT_GROUP_NAME = "我的好友";
   //电子邮件相关
   public final static String SUBJECT = "LayIM即时通讯系统邮箱激活邮件";
  //Redis key相关
   public final static String ONLINE_USER = "ONLINE_USER";

   public final static int SYSTEM_PAGE = 6;

   public final static int USER_PAGE = 21;

   public final static int ADD_MESSAGE_PAGE = 4;
}
