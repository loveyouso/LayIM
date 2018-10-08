package com.chat.service;

import com.chat.common.SystemConstant;
import com.chat.domain.AddInfo;
import com.chat.domain.FriendList;
import com.chat.domain.GroupList;
import com.chat.domain.GroupNumber;
import com.chat.entity.*;
import com.chat.repository.UserMapper;
import com.sun.mail.imap.protocol.ID;
import lombok.val;
import org.apache.catalina.security.SecurityUtil;
import org.aspectj.weaver.ast.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 用户信息相关操作
 * @author loveyouso
 * @create 2018-09-11 10:49
 **/
@Service
public class UserService implements UserMapper{

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * @description: 退出群
     * @param groupNumber 群成员
     * @return: int
     */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"},allEntries = true)
    @Override
    public int leaveOutGroup(GroupNumber groupNumber) {
        return userMapper.leaveOutGroup(groupNumber);
    }

    @Autowired
    private UserMapper userMapper;

    /**
    * @Description: 添加群成员
    * @Param: groupNumber 群成员
    * @return: int
    */
    @Transactional
    @Override
    public int addGroupMember(GroupNumber groupNumber) {
        return userMapper.addGroupMember(groupNumber);
    }

    /**
     * @description: 删除好友
     * @param friendId 好友id
     * @param uId 个人id
     * @return: int
    */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"},allEntries = true)
    @Override
    public int removeFriend(Integer friendId, Integer uId) {
        if (friendId == null || uId == null){
            return 0;
        }else {
            return userMapper.removeFriend(friendId,uId);
        }
    }

    /**
     * @Description: 更新用户头像
     * @param userId 用户id
     * @param avatar 头像地址
     * @return: int
    */
    @CacheEvict(value = "findUserById", allEntries = true)
    @Transactional
    @Override
    public int updateAvatar(Integer userId, String avatar) {
        if (userId == null || avatar == null)
            return 0;
        else
            return userMapper.updateAvatar(userId,avatar);
    }

    /**
     * @Description: 移动好友分组
     * @param groupId 新的分组id
     * @param uId 被移动的好友id
     * @param mId 我的id
     * @return: int
     */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"},allEntries = true)
    @Transactional
    @Override
    public boolean changeGroup(Integer groupId, Integer uId, Integer mId) {
        if (groupId == null || uId == null || mId == null)
            return false;
        else
            return userMapper.changeGroup(groupId,uId,mId);
    }

    /** 
     * @description: 添加好友 
     * @param addFriends	 
     * @return: int
    */
    @Transactional
    @Override
    public int addFriend(AddFriends addFriends) {
        return 0;
    }

    /**
     * @description:  添加好友
     * @param mid
     * @param mgid
     * @param uid
     * @param tgid
     * @param messageBoxId 消息盒子id
     * @return: boolean
    */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"},allEntries = true)
    @Transactional
    public boolean addFriend(Integer mid,Integer mgid,Integer uid,Integer tgid,Integer messageBoxId){
        AddFriends addFriends = new AddFriends(mid,mgid,uid,tgid);
        if(userMapper.addFriend(addFriends) != 0){
            return updateAddMessage(messageBoxId,1);
        }
        return false;
    }

    /**
     * @description: 统计未处理消息
     * @param uid
     * @param agree
     * @return: java.lang.Integer
    */
    @Override
    public Integer countUnHandMessage(Integer uid, Integer agree) {
        return userMapper.countUnHandMessage(uid,agree);
    }

    /** 
     * @description: 查询添加好友、群组信息
     * @param uid 发送给谁的申请,可能是群，那么就是创建该群组的用户
     * @return: java.util.List<com.chat.domain.AddInfo>
    */ 
    @Override
    public List<AddInfo> findAddInfo(Integer uid) {
        List<AddInfo> list = userMapper.findAddInfo(uid);
        for(AddInfo info : list){
            if (info.getType() == 0) {
                info.setContent("申请添加你为好友");
            } else {
                GroupList group = userMapper.findGroupById(info.getFrom_group());
                info.setContent("申请加入 '" + group.getGroupName() + "' 群聊中!");
            }
            info.setHref(null);
            info.setUser(findUserById(info.getFrom()));
            LOGGER.info(info.toString());
        }
        return list;
    }

    /** 
     * @description: 更新好友、群组请求信息
     * @param addMessage	 
     * @return: boolean
    */
    @Transactional
    @Override
    public boolean updateAddMessage(AddMessage addMessage) {
        return userMapper.updateAddMessage(addMessage);
    }

    public boolean updateAddMessage(Integer messageBoxId,Integer agree){
        AddMessage addMessage = new AddMessage();
        addMessage.setAgree(agree);
        addMessage.setId(messageBoxId);
        return userMapper.updateAddMessage(addMessage);
    }

    /**
     * @description: 添加好友、群组请求信息
     * @param addMessage
     * @return: int
    */
    @Override
    public int saveAddMessage(AddMessage addMessage) {
        return userMapper.saveAddMessage(addMessage);
    }

    /**
     * @description: 根据群名模糊统计
     * @param groupName	群名
     * @return: int
    */
    @Override
    public int countGroup(String groupName) {
        return userMapper.countGroup(groupName);
    }

    /**
     * @description: 根据群名模糊查询群
     * @param groupName
     * @return: java.util.List<com.chat.domain.GroupList>
    */
    @Override
    public List<GroupList> findGroup(String groupName) {
        return userMapper.findGroup(groupName);
    }

    @Override
    public GroupList findGroupById(Integer gid) {
        return null;
    }

    /**
     * @description: 根据用户名和性别统计用户
     * @param username	用户名
     * @param sex	性别
     * @return: int
    */
    @Override
    public int countUser(String username, Integer sex) {
        return userMapper.countUser(username,sex);
    }

    /**
     * @description: 根据用户名和性别查询用户
     * @param username	用户名
     * @param sex	性别
     * @return: java.util.List<com.chat.entity.User>
    */
    @Override
    public List<User> findUser(String username, Integer sex) {
        return userMapper.findUser(username,sex);
    }

    /** 
     * @description: 统计查询信息
     * @param uid 消息所属用户id
     * @param mid 消息来自那个用户id
     * @param Type 消息类型、可能是friend或group
     * @return: int
    */ 
    @Override
    public int countHistoryMessage(Integer uid, Integer mid, String Type) {
        if(Type == "friend"){
            return userMapper.countHistoryMessage(uid, mid, Type);
        }else if(Type == "group"){
            return userMapper.countHistoryMessage(null, mid, Type);
        }else {
            return 0;
        }
    }

    @Override
    public List<Receive> findHistoryMessage(Integer uid, Integer mid, String Type) {
        return null;
    }

    /**
     * @description: 查询历史消息
     * @param user
     * @param mid
     * @param Type
     * @return: java.util.List<com.chat.entity.ChatHistory>
    */
    public List<ChatHistory> findHistoryMessage(User user, Integer mid, String Type) {
        List chatHistories = new ArrayList<ChatHistory>();
        //单人聊天记录
        if ("friend".equals(Type)){
            //查找聊天记录
            List<Receive> historys = userMapper.findHistoryMessage(user.getId(),mid,Type);
            User toUser = findUserById(mid);
            for (Receive history:historys){
                ChatHistory chatHistory = null;
                if(history.getId() == mid){
                    chatHistory = new ChatHistory(history.getId(),toUser.getUsername(),toUser.getAvatar(),history.getContent(),history.getTimeStamp());
                }else{
                    chatHistory = new ChatHistory(history.getId(),user.getUsername(),user.getAvatar(),history.getContent(),history.getTimeStamp());
                }
                chatHistories.add(chatHistory);
            }
        }
        //群聊天记录
        if ("group".equals(Type)){
            //查找聊天记录
            List<Receive> historys = userMapper.findHistoryMessage(null,mid,Type);
            for (Receive history:historys){
                ChatHistory chatHistory = null;
                User u = findUserById(history.getFromId());
                if(history.getFromId().equals(user.getId())){
                    chatHistory = new ChatHistory(user.getId(),user.getUsername(),user.getAvatar(),history.getContent(),history.getTimeStamp());
                }else{
                    chatHistory = new ChatHistory(history.getId(),u.getUsername(),u.getAvatar(),history.getContent(),history.getTimeStamp());
                }
                chatHistories.add(chatHistory);
            }
        }

        return chatHistories;
    }

    /**
     * @description: 查询离线消息
     * @param uid
     * @param status
     * @return: java.util.List<com.chat.entity.Receive>
    */
    @Override
    public List<Receive> findOffLineMessage(Integer uid, Integer status) {
        return userMapper.findOffLineMessage(uid,status);
    }

    /**
     * @description: 保存聊天记录
     * @param receive 聊天记录信息
     * @return: int
    */
    @Override
    public int saveMessage(Receive receive) {
        return userMapper.saveMessage(receive);
    }

    /**
     * @description: 更新签名
     * @param sign
     * @param uid
     * @return: int
    */
    @Override
    public boolean updateSign(String sign, Integer uid) {
        if(sign != null || uid != null)
            return userMapper.updateSign(sign,uid);
        else
            return false;
    }

    /** 
     * @description: 激活码激活用户 
     * @param activeCode	 
     * @return: int
    */ 
    @Override
    public int activeUser(String activeCode) {
        if (activeCode == null || "".equals(activeCode))
            return 0;
        else
            return userMapper.activeUser(activeCode);
    }

    /** 
     * @description: 根据群组ID查询群里用户的信息 
     * @param gid	 
     * @return: java.util.List<com.chat.entity.User>
    */
    @Cacheable(value = "findUserByGroupId",keyGenerator = "wiselyKeyGenerator")
    @Override
    public List<User> findUserByGroupId(int gid) {
        List<User> list = userMapper.findUserByGroupId(gid);
        return list;
    }

    /** 
     * @description: 根据id查询用户信息 
     * @param id	 
     * @return: com.chat.entity.User
    */
    @Cacheable(value = "findUserById",keyGenerator = "wiselyKeyGenerator")
    @Override
    public User findUserById(Integer id) {
        if(id != null)
            return userMapper.findUserById(id);
        else
            return null;
    }

    /**
     * @description 根据ID查询用户群组列表
     * @param uid
     * @return
     */
    @Cacheable(value = "findGroupsById",keyGenerator = "wiselyKeyGenerator")
    @Override
    public List<GroupList> findGroupsById(int uid) {
        return userMapper.findGroupsById(uid);
    }

    /** 
     * @description: 根据ID查询用户好友分组列表信息
     * @param uid	用户id
     * @return: java.util.List<com.chat.domain.FriendList>
    */ 
    @Override
    public List<FriendList> findFriendGroupsById(int uid) {
        List<FriendList> friends = userMapper.findFriendGroupsById(uid);
        //封装分组列表下的好友信息
        for (FriendList friend : friends){
            friend.setList(userMapper.findUsersByFriendGroupIds(friend.getId())); 
        }
        return friends;
        
    }

    @Override
    public List<User> findUsersByFriendGroupIds(int fgid) {
        return null;
    }

    /** 
     * @description: 保存用户信息
     * @param user	用户
     * @return: int
    */
    @CacheEvict(value = {"findUserById","findFriendGroupsById","findUserByGroupId"}, allEntries = true)
    @Transactional
    @Override
    public int saveUser(User user) {
        if(user == null || user.getPassword() == null || user.getUsername() == null || user.getEmail() == null){
            return 0;
        }else{
            //加密密码
            String password = user.getPassword();
            password = new BCryptPasswordEncoder().encode(password);
            user.setPassword(password);
            userMapper.saveUser(user);
            LOGGER.info("userid = " + user.getId());
            createFriendGroup(SystemConstant.DEFAULT_GROUP_NAME,user.getId());
        }
        return 1;
    }

    /** 
     * @description: 创建好友分组 
     * @param groupName	
     * @param uid	 
     * @return: void
    */ 
    private void createFriendGroup(String groupName, Integer uid) {
        if (uid == null || groupName == null || "".equals(uid) || "".equals(groupName))
            LOGGER.info("创建好友分组出错");
        else
            userMapper.createFriendGroup(new FriendGroup(uid, groupName));
    }

    @Override
    public User matchUser(String email) {
        return null;
    }

    /**
     * @description: 用户邮件和密码是否匹配
     * @param user
     * @return: com.chat.entity.User
    */
    public User matchUser(User user){
        if(user == null || user.getEmail() == null){
            return null;
        }
        User u = userMapper.matchUser(user.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (u == null || encoder.matches(u.getPassword(),user.getPassword())){
            return null;
        }
        return u;
    }


    @Override
    public int createFriendGroup(FriendGroup friendGroup) {
        return 0;
    }

    /**
     * @description 判断邮件是否存在
     * @param email
     * @return
     */
    public boolean existEmail(String email){
        if (email == null || "".equals(email))
            return false;
        if(userMapper.matchUser(email) != null){
            return true;
        }
        return  false;
    }
}
