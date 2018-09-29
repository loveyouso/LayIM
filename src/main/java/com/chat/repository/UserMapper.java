package com.chat.repository;

import com.chat.domain.AddInfo;
import com.chat.domain.FriendList;
import com.chat.domain.GroupList;
import com.chat.domain.GroupNumber;
import com.chat.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @author loveyouso
 * @create 2018-09-11 10:47
 **/
@Mapper     //指定这是一个操作数据库的mapper
public interface UserMapper {

    /**
     * @Description: 退出群
     * @Param: [groupNumber]
     * @return: int
     */
    @Delete("delete from t_group_members where gid=#{gid} and uid=#{uid}")
    int leaveOutGroup(GroupNumber groupNumber);

    /** 
    * @Description: 添加群成员
    * @Param: gid 群编号
    * @Param: uid 用户编号
    * @return: int
    */ 
    @Insert("insert into t_group_members(gid,uid) values(#{gid},#{uid})")
    int addGroupMember(GroupNumber groupNumber);

    /**
    * @Description: 删除好友
    * @Param: [friendId 好友id, uId 个人id]
    * @return: int
    */
    @Delete("delete from t_friend_group_friends where fgid in (select id from t_friend_group where uid in (#{friendId},#{uId})) and uid in (#{friendId},#{uId})")
    int removeFriend(Integer friendId,Integer uId);

    /**
    * @Description: 更新用户头像
    * @Param: [userId 用户id, avatar 头像地址]
    * @return: int
    */
    @Update("update t_user set avatar=#{avatar} where id=#{userId}")
    int updateAvatar(Integer userId,String avatar);

    /** 
    * @Description: 移动好友分组
    * @Param: [groupId 新的分组id, uId 被移动的好友id, mId 我的id]
    * @return: int
    */ 
    @Update("update t_friend_group_friends set fgid = #{groupId} where id =(select t.id from ((select id from t_friend_group_friends where fgid in (select id from t_friend_group where uid = #{mId}) and uid = #{uId}) t))")
    boolean changeGroup(Integer groupId,Integer uId,Integer mId);

    /**
    * @Description: 添加好友操作
    * @Param: [addFriends]
    * @return: int
    */
    @Insert("insert into t_friend_group_friends(fgid,uid) values(#{mgid},#{tid}),(#{tgid},#{mid})")
    int addFriend(AddFriends addFriends);

    /**
    * @Description: 统计未处理的消息
    * @Param: [uid, agree]
    * @return: java.lang.Integer
    */
    @Select("<script> select count(*) from t_add_message where to_uid=#{uid} <if test='agree!=null'> and agree=#{agree} </if> </script>")
    Integer countUnHandMessage(Integer uid,Integer agree);

    /**
    * @Description: 查询添加好友、群组信息
    * @Param: [uid]
    * @return: java.util.List<com.chat.domain.AddInfo>
    */
    @Select("select * from t_add_message where to_uid = #{uid} order by time desc")
    @Results({
            @Result(property = "from",column = "from_uid"),
            @Result(property = "uid" , column = "to_uid"),
            @Result(property = "read", column = "agreee"),
            @Result(property = "from_group",column = "group_id")
    })
    List<AddInfo> findAddInfo(Integer uid);

    /**
    * @Description: 更新好友、群组信息请求
    * @Param: [addMessage]
    * @return: int
    */
    @Update("update t_add_message set agree = #{agree} where id = #{id}")
    boolean updateAddMessage(AddMessage addMessage);

    /**
    * @Description: 添加好友、群组信息请求
    * @Param: [addMessage]
    * @return: int
    */
    @Insert("insert into t_add_message(form_uid,to_uid,group_id,remark,agree,type,time) " +
            "values(#{fromUid},#{toUid},#{groupId},#{remark},#{agree},#{type},#{time}) " +
            "ON DUPLICATE KEY UPDATE remark=#{remark},time=#{time},agree=#{agree};")
    int saveAddMessage(AddMessage addMessage);

    /**
    * @Description: 根据群名模糊统计群
    * @Param: [groupName]
    * @return: int
    */
    @Select("<script> select count(*) from t_group where 1 = 1 <if test='groupName!=null'> and  group_name like '%${groupName}%'</if> </script>")
    int countGroup(String groupName);

    /**
    * @Description: 根据群名模糊查询群
    * @Param: [groupName]
    * @return: java.util.List<com.chat.domain.GroupList>
    */
    @Select("<script> select id,group_name,avatar,create_id from t_group where 1=1 <if test='groupName != null'> and group_name like '%${groupName}%'</if></script>")
    List<GroupList> findGroup(String groupName);

    /**
    * @Description: 根据群id查询群信息
    * @Param: [gid]
    * @return: com.chat.domain.GroupList
    */
    @Select("select id,group_name,avatar,create_id from t_group where id = #{gid}")
    GroupList findGroupById(Integer gid);

    /**
    * @Description: 根据用户名和性别统计用户
    * @Param: [username, sex]
    * @return: int
    */
    @Select("<script> select count(*) from t_user " +
            "where 1 = 1 <if test='username != null'> and username like '%${username}%'</if>" +
                        "<if test='sex != null'> and sex = #{sex}</if> </script>")
    int countUser(String username,Integer sex);

    /**
    * @Description: 根据用户名和性别查询用户
    * @Param: [username, sex]
    * @return: java.util.List<com.chat.entity.User>
    */
    @Select("<script> select id,username,status,sign,acatar,email from t_user " +
            "where 1 = 1 <if test='username != null'>and username like '%${username}'</if>" +
                        "<if test='sex != null'>and sex = #{sex}</if> </script>")
    List<User> findUser(String username,Integer sex);

    /** 
    * @Description: 统计查询信息
    * @Param: [uid, mid, Type] 
    * @return: int
    */ 
    @Select("<script> select count(*) from t_message where type = #{Type} and " +
            "<choose><when test='uid!=null and mid !=null'>(toid = #{uid} and mid = #{mid}) or (toid = #{mid} and mid = #{uid}) </when>" +
            "<when test='mid != null'> mid = #{mid} </when></choose> order by timestamp </script>")
    int countHistoryMessage(Integer uid,Integer mid,String Type);

    /**
    * @Description:  查询消息
    * @Param: [uid, mid, Type]
    * @return: java.util.List<com.chat.entity.Receive>
    */
    @Results({
            @Result(property = "id",column = "mid")
    })
    @Select("<script> select toid,fromid,mind,content,type,timestamp,status from t_message where type = #{Type} and" +
            "<choose><when test='uid!=null and mid !=null'>(toid = #{uid} and mid = #{mid}) or (toid = #{mid} and mid = #{uid}) </when>" +
            "<when test='mid != null'> mid = #{mid} </when></choose> order by timestamp </script>")
    List<Receive> findHistoryMessage(Integer uid,Integer mid,String Type);

    /** 
    * @Description: 查询消息 
    * @Param: [uid, status] 
    * @return: java.util.List<com.chat.entity.Receive>
    */ 
    @Results({
            @Result(property = "id",column = "mid")
    })
    @Select("select toid,fromid,mid,content,type,timestamp,status from t_message where touid = #{uid} and status = #{status}")
    List<Receive> findOffLineMessage(Integer uid,Integer status);

    /**
    * @Description: 保存用户聊天记录
    * @Param: [receive]
    * @return: int
    */
    @Insert("insert into t_message(mid,toid,fromid,content,type,timestamp,status) values(#{id},#{toid},#{fromid},#{content},#{type},#{timestamp},#{status})")
    int saveMessage(Receive receive);

    /**
    * @Description: 更新用户签名
    * @Param: [sign, uid]
    * @return: int
    */
    @Update("update t_user set sign = #{sign} where id = #{uid}")
    boolean updateSign(String sign,Integer uid);

    //如果不用邮箱激活策略，此代码可注释
    /**
    * @Description: 激活用户账号
    * @Param: [activeCode]
    * @return: int
    */
    @Update("update t_user set staus = 'offline' where active = #{activeCode}")
    int activeUser(String activeCode);

    /**
    * @Description: 根据群组id查询群里用户的信息
    * @Param: [gid]
    * @return: java.util.List<com.chat.entity.User>
    */
    @Select("select id,username,status,sign,avatar,email from t_user " +
            "where id in(select uid from t_group_members where gid = #{gid})")
    List<User> findUserByGroupId(int gid);

    /**
    * @Description: 根据用户id查询用户的消息
    * @Param: [id]
    * @return: com.chat.entity.User
    */
    @Select("select id,username,status,sign,avatar,email,sex from t_user where id = #{id}")
    User findUserById(Integer id);

    /**
     * @Description: 根据某个用户的uid查询 用户群组列表 ，不论是自己创建的还是别人创建的
     * @Param: [uid]
     * @return: java.util.List<com.chat.domain.GroupList>
     */
    @Results({
            @Result(property = "createId",column = "create_id")
    })
    @Select("select id,group_name,acatar,create_id from t_group " +
            "where id in (select distinct gid from t_group_members where uid = #{uid})")
    List<GroupList> findGroupsById(int uid);

    /**
    * @Description: 根据用户id查询用户好友分组列表
    * @Param: [uid]
    * @return: java.util.List<com.chat.domain.FriendList>
    */
    @Select("select id,group_name from t_friend_group where uid = #{uid}")
    List<FriendList> findFriendGroupsById(int uid);

    /** 
    * @Description: 根据好友列表id查询用户信息 
    * @Param: [fgid] 
    * @return: java.util.List<com.chat.entity.User>
    */ 
    @Select("select id,username,avatar,sign,status,email,sex from t_user " +
            "where id in (select uid from t_friend_group_friends where fgid = #{fgid})")
    List<User> findUsersByFriendGroupIds(int fgid);

    /**
    * @Description: 保存用户信息
    * @Param: [user]
    * @return: int
    */
    @Insert("insert into t_user(username,password,email,create_date,active) " +
            "values(#{username},#{password},#{email},#{createDate},#{active})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveUser(User user);

    /**
    * @Description:  根据邮箱匹配用户
    * @Param: [email]
    * @return: com.chat.entity.User
    */
    @Select("select id,username,email,avatar,sex,sign,password,status,active from t_user " +
            "where email = #{email}")
    User matchUser(String email);

    /** 
    * @Description: 创建好友分组列表 
    * @Param: [friendGroup] 
    * @return: int
    */ 
    @Insert("insrt into t_friend_group(group_name,uid) value(#{groupName},#{uid})")
    int createFriendGroup(FriendGroup friendGroup);
}
