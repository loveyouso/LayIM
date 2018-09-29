package com.chat.util;

import com.chat.common.SystemConstant;
import com.chat.domain.GroupNumber;
import com.chat.entity.*;
import com.chat.service.RedisService;
import com.chat.service.UserService;
import com.chat.websocket.domain.AgreeAddGroup;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

/**
 * @description WebSocket工具类(单例)
 * @author loveyouso
 * @create 2018-09-19 12:14
 **/
public class WebSocketUtil {

    private Logger LOGGER = LoggerFactory.getLogger(WebSocketUtil.class);

    private static WebSocketUtil instance = new WebSocketUtil();

    private WebSocketUtil() {
    }

    public static WebSocketUtil getInstance(){
        return instance;
    }

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    private final Gson gson = new Gson();

    @Getter
    private final Map<Integer, Session> sessions = Collections.synchronizedMap(new HashMap<Integer, Session>());

    /**
     * @description 发送消息
     * @message Message
     */
    public synchronized void sendMessage(Message message){
        LOGGER.info("发送好友消息和群消息!");
        //封装返回消息格式
        Integer gid = message.getTo().getId();
        Receive receive = getReceiveType(message);
        Integer key = message.getTo().getId();
        //聊天类型，可能来自朋友或群组
        if("friend".equals(message.getTo().getType())) {
            //是否在线
            if(getSessions().containsKey(key)) {
                Session session = getSessions().get(key);
                receive.setStatus(1);
                sendMessage(gson.toJson(receive).replaceAll("Type", "type"), session);
            }
            //保存为离线消息,默认是为离线消息
            userService.saveMessage(receive);
        } else {
            receive.setId(gid);
            //找到群组id里面的所有用户
            List<User> users= userService.findUserByGroupId(gid);
            for (User user:users){
                //过滤掉本身的uid
                if(user.getId() != message.getMine().getId()){
                    //是否在线
                    if(getSessions().containsKey(user.getId())) {
                        Session session = getSessions().get(user.getId());
                        receive.setStatus(1);
                        sendMessage(gson.toJson(receive).replaceAll("Type", "type"), session);
                    } else {
                        receive.setId(key);
                    }
                }
            }
            //保存为离线消息
            userService.saveMessage(receive);
        }
    }

    /**
     * @description 同意添加好友
     * @param message
     */
    public void agreeAddGroup(Message message){
        AgreeAddGroup agree = gson.fromJson(message.getMsg(),AgreeAddGroup.class);
        GroupNumber groupNumber = new GroupNumber(agree.getGroupId(),agree.getToUid());
        userService.addGroupMember(groupNumber);
        userService.updateAddMessage(agree.getMessageBoxId(),1);
    }

    /**
     * @description 拒绝添加群
     * @param message
     */
    public void refuseAddGroup(Message message){
        AgreeAddGroup refuse = gson.fromJson(message.getMsg(), AgreeAddGroup.class);
        userService.updateAddMessage(refuse.getMessageBoxId(), 2);
    }

    /**
     * @description 通知对方删除好友
     * @param uId 我的id
     * @param friendId 对方Id
     */
    public synchronized void removeFriend(Integer uId,Integer friendId) {
        HashMap<String,String> result = new HashMap<String, String>();
        if (sessions.get(friendId) != null){
            result.put("type", "delFriend");
            result.put("uId", uId + "");
            sendMessage(gson.toJson(result), sessions.get(friendId));
        }
    }

    /**
     * @description 添加群组
     * @param uid
     * @param message
     */
    public synchronized void addGroup(Integer uid, Message message){
        AddMessage addMessage = new AddMessage();
        Mine mine = message.getMine();
        To to = message.getTo();
        com.chat.websocket.domain.Group t = gson.fromJson(message.getMsg(), com.chat.websocket.domain.Group.class);
        addMessage.setFromUid(mine.getId());
        addMessage.setToUid(to.getId());
        addMessage.setTime(new DateUtil().getDateTime());
        addMessage.setGroupId(t.getGroupId());
        addMessage.setRemark(t.getRemark());
        addMessage.setType(1);
        userService.saveAddMessage(addMessage);
        HashMap<String,String> result = new HashMap<String, String>();
        if (sessions.get(to.getId()) != null) {
            result.put("type", "addGroup");
            sendMessage(gson.toJson(result), sessions.get(to.getId()));
        }
    }

    /**
     * @description 添加好友
     * @param uid
     * @param message
     */
    public synchronized void addFriend(Integer uid,Message message){
        Mine mine = message.getMine();
        AddMessage addMessage = new AddMessage();
        addMessage.setFromUid(mine.getId());
        addMessage.setTime(new DateUtil().getDateTime());
        addMessage.setToUid(message.getTo().getId());
        Add add = gson.fromJson(message.getMsg(), Add.class);
        addMessage.setRemark(add.getRemark());
        addMessage.setType(add.getType());
        addMessage.setGroupId(add.getGroupId());
        userService.saveAddMessage(addMessage);
        HashMap<String,String> result = new HashMap<String,String>();
        //如果对方在线，则推送给对方
        if (sessions.get(message.getTo().getId()) != null) {
            result.put("type", "addFriend");
            sendMessage(gson.toJson(result), sessions.get(message.getTo().getId()));
        }
    }

    /**
     * @description 统计离线消息数量
     * @param uid
     */
    public synchronized HashMap<String,String> countUnHandMessage(Integer uid){
        Integer count = userService.countUnHandMessage(uid,0);
        LOGGER.info("Number of offline = " + count);
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("type","unHandMessage");
        result.put("count",count + "");
        return result;
    }

    /**
     * @description 监测某个用户的离线或者在线
     * @param message
     */
    public synchronized HashMap<String, String> checkOnline(Message message,Session session){
        LOGGER.info("监测在线状态" + message.getTo().toString());
        Set uids = redisService.getSets(SystemConstant.ONLINE_USER);
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("type","checkOnline");
        if (uids.contains(message.getTo().getId().toString())){
            result.put("status","在线");
        }else{
            result.put("status","离线");
        }
        return result;
    }

    /**
     * @description 发送消息
     * @param message
     * @param session
     */
    public synchronized void sendMessage(String message,Session session){
        try {
            //getAsyncRemote是非阻塞式的(异步)，getBasicRemote是阻塞式的(同步)
            //使用getBasicRemote，第二行的消息必须等待第一行的发送完成才能进行
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOGGER.error("发送消息失败");
            e.printStackTrace();
        }
    }

    /**
     * @description 封装返回消息格式
     * @param message
     * @return Receive
     */
    public Receive getReceiveType(Message message){
        Mine mine = message.getMine();
        To to = message.getTo();
        Receive receive = new Receive();
        receive.setId(mine.getId());
        receive.setFromId(to.getId());
        receive.setUsername(mine.getUsername());
        receive.setType(to.getType());
        receive.setAvatar(mine.getAvatar());
        receive.setContent(mine.getContent());
        receive.setTimeStamp(new DateUtil().getLongDateTime());
        return receive;
    }

    /**
     * @description 用户在线切换状态
     * @param uid 用户id
     * @param status 状态
     */
    public synchronized  void changeOnline(Integer uid,String status){
        if ("online".equals(status)){
            redisService.set(SystemConstant.ONLINE_USER,uid + "");
        }else{
            redisService.removeSetValue(SystemConstant.ONLINE_USER,uid + "");
        }
    }

}
