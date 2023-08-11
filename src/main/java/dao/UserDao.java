package dao;

import pojo.Message;
import pojo.User;

import java.sql.Connection;
import java.util.List;

/**
 * 用于规范users表里的通用操作
 */
public interface UserDao {

    /**
     * 管理员首页展示用户基本信息4个（user_id,user_name,is_freezing,create_time）
     * @return
     */
    List<User>getManagerIndex(Connection connection);

    /**
     *
     * @param username
     * @param password
     * @param userId
     * @return
     */
    Message register(String username,String password,String userId);


    /**
     * 根据链接获取全部信息
     * @param
     * @return
     */
    List<User> getAllUsers();


    /**
     *
     * @return
     */
    User getUserInformation(String userId,Connection connection,Message message);

    /**
     *
     * @param connection
     * @param username
     * @param password
     * @param userId
     * @return
     */
    boolean insertUsers(Connection connection, String username, String password, String userId);

    /**
     *插入用户角色
     * @param connection
     * @param userId
     * @return
     */
    boolean insertUserRole(Connection connection,String userId);

    /**
     *获得关注数
     * @param connection
     * @param userId
     * @param message
     * @return
     */
    Integer getFollowNum(Connection connection,String userId,Message message);

    /**
     *获得粉丝数
     * @param connection
     * @param userId
     * @param message
     * @return
     */
    Integer getFansNum(Connection connection,String userId,Message message);

    /**
     *获得收藏数
     * @param connection
     * @param userId
     * @param message
     * @return
     */
    Integer getCollectionNum(Connection connection,String userId,Message message);

    /**
     *判断是否已经关注
     * @param connection
     * @param authorId
     * @param userId
     * @return
     */
    Integer judgeFollow(Connection connection,String authorId,String userId);

    /**
     *对关注进行操作
     * @param connection
     * @param flag
     * @param authorId
     * @param userId
     * @return
     */
    Boolean operateFollow(Connection connection,int flag,String authorId,String userId);

    /**
     * 获取修改后的昵称进行修改
     * @param connection
     * @param username
     * @param userid
     * @return
     */
    String changeUser(Connection connection,String username ,String userid);

    /**查询用户
     * @param connection
     * @param keyword
     * @return
     */
    List<User> searchUsers(Connection connection,String keyword);

    /**
     * 删除用户（管理员）
     * @param connection
     * @param userId
     * @return
     */
    String deleteUser(Connection connection,String userId);
}