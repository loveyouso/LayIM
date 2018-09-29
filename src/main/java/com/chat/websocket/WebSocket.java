package com.chat.websocket;


import com.chat.common.SystemConstant;
import com.chat.entity.Message;
import com.chat.service.RedisService;
import com.chat.util.WebSocketUtil;
import com.google.gson.Gson;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.util.HashMap;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.sym.error;

/**
 * websocket服务器处理消息
 * @author loveyouso
 * @create 2018-09-19 12:08
 **/
@ServerEndpoint(value = "/websocket/{uid}")
@Component
public class WebSocket {

    private Integer uid = null;

    private Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);

    private final Gson gson = new Gson();

    @Autowired
    private RedisService redisService;

    private static WebSocket  webSocket ;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        webSocket = this;
        webSocket.redisService = this.redisService;
        // 初使化时将已静态化的testService实例化
    }

    /**
     * @description 服务器接收到消息调用
     * @param message 消息体
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Message mess = gson.fromJson(message.replaceAll("type", "Type"), Message.class);
        LOGGER.info("来自客户端的消息: " + mess);
        switch (mess.getType()){
            case "message":
                WebSocketUtil.getInstance().sendMessage(mess);
                break;
            case"checkOnline":
                WebSocketUtil.getInstance().checkOnline(mess,session);
                break;
            case "addGroup":
                WebSocketUtil.getInstance().addGroup(uid, mess);
                break;
            case "changOnline":
                WebSocketUtil.getInstance().changeOnline(uid, mess.getMsg());
                break;
            case "addFriend" :
                WebSocketUtil.getInstance().addFriend(uid, mess);
                break;
            case "agreeAddFriend":
                if (WebSocketUtil.getInstance().getSessions().get(mess.getTo().getId()) != null) {
                    WebSocketUtil.getInstance().sendMessage(message, WebSocketUtil.getInstance().getSessions().get(mess.getTo().getId()));
                }
                break;
            case "agreeAddGroup" :
                WebSocketUtil.getInstance().agreeAddGroup(mess);
                break;
            case "refuseAddGroup" :
                WebSocketUtil.getInstance().refuseAddGroup(mess);
                break;
            case "unHandMessage" :
                HashMap<String,String> result = WebSocketUtil.getInstance().countUnHandMessage(uid);
                WebSocketUtil.getInstance().sendMessage(gson.toJson(result), session);
                break;
            case "delFriend" :
                WebSocketUtil.getInstance().removeFriend(uid, mess.getTo().getId());
                break;
            default:
                LOGGER.info("No Mapping Message!");
        }
    }

    /**
     * @description 首次创建链接
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("uid") Integer uid){
        this.uid = uid;
        WebSocketUtil.getInstance().getSessions().put(uid,session);
        LOGGER.info("userId = " + uid + ",sessionId = " + session.getId() + ",新连接加入!");
        webSocket.redisService.setSet(SystemConstant.ONLINE_USER,uid + "");
    }

    /**
     * @description 链接关闭调用
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        LOGGER.info("userId = " + uid + ",sessionId= " + session.getId() + "断开连接");
        WebSocketUtil.getInstance().getSessions().remove(uid);
        webSocket.redisService.removeSetValue(SystemConstant.ONLINE_USER,uid + "");
    }

    /**
     * @description 服务器发送错误调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session,Throwable error){
        LOGGER.info(session.getId() + " 发生错误==Error message:" );
        error.printStackTrace();
        onClose(session);
    }
}
