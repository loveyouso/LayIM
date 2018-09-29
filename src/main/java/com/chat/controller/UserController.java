package com.chat.controller;

import com.chat.common.SystemConstant;
import com.chat.domain.*;
import com.chat.entity.ChatHistory;
import com.chat.entity.Receive;
import com.chat.entity.User;
import com.chat.service.UserService;
import com.chat.util.FileUtil;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author loveyouso
 * @create 2018-09-11 10:00
 **/
@Controller
@Api(value = "用户相关操作Controller")
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    private FileUtil fileUtil;

//    @Autowired
//    private UserServiceImpl userService;

    private final Gson gson = new Gson();

    /**
     * @description: 退出群
     * @param groupId	群编号
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/leaveOutGroup",method = RequestMethod.POST)
    @ResponseBody
    public String leaveOutGroup(@RequestParam("groupId") Integer groupId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        GroupNumber groupNumber = new GroupNumber();
        groupNumber.setGid(groupId);
        groupNumber.setUid(user.getId());
        Object result = userService.leaveOutGroup(groupNumber);
        return gson.toJson(new ResultSet<>(result));
    }

    /** 
     * @description: 注册 
     * @param uesr	 
     * @return: java.lang.String
    */ 
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody User uesr){
        if(userService.saveUser(uesr) == 1){
            return gson.toJson(new ResultSet<String>(SystemConstant.SUCCESS,SystemConstant.SUCCESS_MESSAGE));
        }else{
            return gson.toJson(new ResultSet<String>(SystemConstant.ERROR,SystemConstant.ERROR_MESSAGE));
        }
    }

    /**
     * @description: 登录
     * @param user
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody User user,HttpServletRequest request){
        User u = userService.matchUser(user);
        if (u != null && "nonactivated".equals(user.getStatus())){
            return gson.toJson(new ResultSet<User>(SystemConstant.ERROR,SystemConstant.LOGGIN_FAIL));
        }else if(u != null && !"nonactivated".equals(user.getStatus())){
            LOGGER.info(user + "成功登陆服务器");
            request.getSession().setAttribute("user",u);
            return gson.toJson(new ResultSet<>(u));
        }else{
            return gson.toJson(new ResultSet<User>(SystemConstant.ERROR,SystemConstant.LOGGIN_FAIL));
        }
    }

    /**
     * @description: 跳转主页
     * @param model
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user",user);
        LOGGER.info("用户" + user + "登录服务器");
        return "index";
    }
    /**
     * @description: 获取群成员
     * @param id
     * @return: java.lang.String
    */
    @RequestMapping(value = "/getMembers",method = RequestMethod.GET)
    @ResponseBody
    public String getMembers(@RequestParam("id") int id){
        List<User> users = userService.findUserByGroupId(id);
        FriendList friends = new FriendList(users);
        return gson.toJson(new ResultSet<>(friends));
    }

    /** 
     * @description: 初始化界面数据 
     * @param userId	 
     * @return: java.lang.String
    */
    @ApiOperation(value = "初始化聊天界面数据，分组列表好友信息、群列表")
    @RequestMapping(value = "/init/{userId}",method = RequestMethod.POST)
    @ResponseBody
    public String init(@PathVariable("userId") int userId){
        FriendAndGroupInfo date = new FriendAndGroupInfo();
        User user = userService.findUserById(userId);
        user.setStatus("online" );
        date.setMine(user);
        date.setFriend(userService.findFriendGroupsById(userId));
        date.setGroup(userService.findGroupsById(userId));
        return gson.toJson(new ResultSet<>(date));
    }

    /**
     * @description:  删除好友
     * @param friendId
     * @param request
     * @return: java.lang.String
    */
    @ResponseBody
    @RequestMapping(value = "/removeFriend",method = RequestMethod.POST)
    public String removeFriend(@RequestParam("friendId") Integer friendId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        int result = userService.removeFriend(friendId,user.getId());
        return gson.toJson(new ResultSet<>(result));
    }

    /**
     * @description: 移动分组好友
     * @param groupId 新的分组id
     * @param uId     要移动的好友id
     * @param request
     * @return: java.lang.String
    */
    public String changeGroup(@RequestParam("groupId") Integer groupId,@RequestParam("uId") Integer uId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        boolean result = userService.changeGroup(groupId,uId,user.getId());
        if (result){
            return gson.toJson(new ResultSet(result));
        }else {
            return gson.toJson(new ResultSet(SystemConstant.ERROR, SystemConstant.ERROR_MESSAGE));
        }
    }

    /**
     * @description:  添加好友
     * @param uid  对方的id
     * @param group  我设定的分组id
     * @param fromGroup  对方设定的分组id
     * @param messageBoxId  消息盒子的消息id
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/agreeFriend",method = RequestMethod.POST)
    @ResponseBody
    public String agreeFriend(@RequestParam("uid") Integer uid,@RequestParam("group") Integer group,
                              @RequestParam("from_group") Integer fromGroup,
                              @RequestParam("messageBoxId") Integer messageBoxId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Object result = userService.addFriend(user.getId(),group,uid,fromGroup,messageBoxId);
        return gson.toJson(new ResultSet<>(result));
    }

    /**
     * @description:  拒绝添加好友
     * @param messageBoxId	消息盒子id
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/refuseFriend",method = RequestMethod.POST)
    @ResponseBody
    public String refuseFriend(@RequestParam("messageBoxId") Integer messageBoxId,HttpServletRequest request){
        Object result = userService.updateAddMessage(messageBoxId,2);
        return gson.toJson(new ResultSet<>(request));
    }

    /**
     * @description:查询消息盒子信息
     * @param uid
     * @param page
     * @return: java.lang.String
    */
    @RequestMapping(value = "/findAddInfo",method =RequestMethod.GET)
    @ResponseBody
    public String findAddInfo(@RequestParam("uid") Integer uid,@RequestParam("page") int page){
        PageHelper.startPage(page,SystemConstant.ADD_MESSAGE_PAGE);
        List list = userService.findAddInfo(uid);
        int count = userService.countUnHandMessage(uid,null);
        int pages = 0;
        if(count < SystemConstant.ADD_MESSAGE_PAGE)
            pages =  1;
        else
            pages =  count/SystemConstant.ADD_MESSAGE_PAGE + 1;
        //此处需要进行Type转换，Type,type
        return gson.toJson(new ResultPageSet(list,pages)).replaceAll("Type","type");
    }

    /**
     * @description: 分页查找好友
     * @param page   第几页
     * @param username  好友名字
     * @param sex  性别
     * @return: java.lang.String
    */
    @RequestMapping(value = "/findUsers",method = RequestMethod.GET)
    @ResponseBody
    public String findUsers(@RequestParam(value = "page",defaultValue = "1") int page,
                            @RequestParam(value = "username",required = false) String username,
                            @RequestParam(value = "sex",required = false) Integer sex){
        int count = userService.countUser(username,sex);
        int pages = 0;
        if(count < SystemConstant.ADD_MESSAGE_PAGE)
            pages =  1;
        else
            pages =  count/SystemConstant.ADD_MESSAGE_PAGE + 1;
        PageHelper.startPage(page,SystemConstant.ADD_MESSAGE_PAGE);
        List users = userService.findUser(username,sex);
        ResultPageSet result = new ResultPageSet();
        result.setData(users);
        result.setPages(pages);
        return gson.toJson(result);
    }

    /**
     * @description: 分页查找分组
     * @param page	第几页
     * @param groupName 群名称
     * @return: java.lang.String
    */
    @RequestMapping(value = "/findGroups",method = RequestMethod.GET)
    @ResponseBody
    public String findGroups(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "name",required = false) String groupName){
        int count = userService.countGroup(groupName);
        int pages = 0;
        if (count < SystemConstant.USER_PAGE)
            pages = 1;
        else
            pages = SystemConstant.USER_PAGE/count + 1;
        PageHelper.startPage(page,SystemConstant.USER_PAGE);
        List groups = userService.findGroup(groupName);
        return gson.toJson(new ResultPageSet(groups,pages));
    }

    /**
     * @description:  获取条记录
     * @param id	与谁的聊天记录的id
     * @param type	类型，friend 或者 group
     * @param page
     * @param request
     * @param model
     * @return: java.lang.String
    */
    @RequestMapping(value = "/chatLog",method = RequestMethod.POST)
    @ResponseBody
    public String chatLog(@RequestParam("id") Integer id, @RequestParam("Type") String type,
                          @RequestParam("page") int page,HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");
        PageHelper.startPage(page,SystemConstant.SYSTEM_PAGE);
        List<ChatHistory> history = userService.findHistoryMessage(user,id,type);
        return gson.toJson(new ResultSet<>(history));
    }

    /**
     * @description:  弹出聊天窗口
     * @param id	与谁的聊天的id
     * @param type	类型，friend 或者 group
     * @param model
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "chatLogIndex",method = RequestMethod.GET)
    public String chatLogIndex(@RequestParam("id") Integer id,@RequestParam("Type") String type,
                               Model model,HttpServletRequest request){
        model.addAttribute("id",id);
        model.addAttribute("Type",type);
        User user = (User) request.getSession().getAttribute("user");
        int pages = userService.countHistoryMessage(user.getId(),id,type);
        if(pages < SystemConstant.SYSTEM_PAGE)
            pages = 1;
        else
            pages = pages/SystemConstant.SYSTEM_PAGE + 1;
        model.addAttribute("pages",pages);
        return "chatLog";
    }

    /**
     * @description: 查询离线消息
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/getOffLineMessage",method = RequestMethod.POST)
    @ResponseBody
    public String getOffLineMessage(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        LOGGER.info("查询 uid = " + user.getId() + " 的离线消息");
        List<Receive> receives = userService.findOffLineMessage(user.getId(),0);
        for(Receive receive:receives){
            User u = userService.findUserById(receive.getId());
            receive.setUsername(u.getUsername());
            receive.setAvatar(u.getAvatar());
        }
        return gson.toJson(new ResultSet<>(receives)).replaceAll("Type","type");
    }

    /**
     * @description:  更新签名
     * @param sign
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/updateSign",method = RequestMethod.POST)
    @ResponseBody
    public String updateSign(@RequestParam("sign") String sign,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        user.setSign(sign);
        if (userService.updateSign(sign,user.getId()))
            return  gson.toJson(new ResultSet<>());
        else
            return gson.toJson(new ResultSet(SystemConstant.ERROR,SystemConstant.ERROR_MESSAGE));
    }

    /**
     * @description:  客户端上传图片
     * @param file
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/upload/image",method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file,HttpServletRequest request){
        if(file.isEmpty()){
            return gson.toJson(new ResultSet(SystemConstant.ERROR,SystemConstant.ERROR_MESSAGE));
        }
        //此处getRealPath()用来获取网站的实际物理路径,如果打包war，可能返回null
        String path = request.getServletContext().getRealPath("/");
        fileUtil = new FileUtil();
        String src = fileUtil.upload(SystemConstant.IMAGE_PATH,path,file);
        HashMap<String,String> result = new HashMap<String,String>();
        //图片的相对地址
        result.put("src",src);
        LOGGER.info("图片" + file.getOriginalFilename() + "上传成功");
        return gson.toJson(new ResultSet<HashMap<String, String>>(result));
    }

    /**
     * @description:  客户端上传文件
     * @param file
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/upload/file",method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file,HttpServletRequest request){
        if(file.isEmpty()){
            return gson.toJson(new ResultSet(SystemConstant.ERROR,SystemConstant.ERROR_MESSAGE));
        }
        //此处getRealPath()用来获取网站的实际物理路径,如果打包war，可能返回null
        String path = request.getServletContext().getRealPath("/");
        fileUtil = new FileUtil();
        String src = fileUtil.upload(SystemConstant.FILE_PATH
                ,path,file);
        HashMap<String,String> result = new HashMap<String,String>();
        //文件的相对地址
        result.put("src",src);
        result.put("name",file.getOriginalFilename());
        LOGGER.info("文件" + file.getOriginalFilename() + "上传成功");
        return gson.toJson(new ResultSet<HashMap<String, String>>(result));
    }

    /**
     * @description:  更新头像
     * @param avatar    头像
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/updateAvatar",method = RequestMethod.POST)
    @ResponseBody
    public String updateAvatar(@RequestParam("avatar") MultipartFile avatar,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        String path = request.getServletContext().getRealPath(SystemConstant.AVATAR_PATH);
        String src = fileUtil.upload(path,avatar);
        userService.updateAvatar(user.getId(),src);
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("src",src);
        return gson.toJson(new ResultSet<HashMap<String,String>>(result));
    }

    /**
     * @description:
     * @param id
     * @param request
     * @return: java.lang.String
    */
    @RequestMapping(value = "/findUser",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String findUserById(@RequestParam("id") Integer id,HttpServletRequest request){
        User user = userService.findUserById(id);
        return gson.toJson(new ResultSet<>(user));
    }

    /**
     * @description 判断邮件是否存在(检测用户重复注册)
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/existEmail", method = RequestMethod.POST)
    public String existEmail(@RequestParam("email")String email){
        return gson.toJson(new ResultSet(userService.existEmail(email)));
    }

}
