package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDaoImp;
import pojo.Message;
import pojo.User;
import utils.DataBaseUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService implements UserInterface{
    @Override
    public Message register(String username, String password, String userId) {
        return new UserDaoImp().register(username,password,userId);
    }

    @Override
    public Message getUser(String userId, ObjectMapper objectMapper) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        UserDaoImp  userDaoImp = new UserDaoImp();
        User user = userDaoImp.getUserInformation(userId,connection,message);
        user.setFollowNum(userDaoImp.getFollowNum(connection,userId,message));
        user.setFansNum(userDaoImp.getFansNum(connection,userId,message));
        user.setCollectionNum(userDaoImp.getCollectionNum(connection,userId,message));

        String s = null;
        try {
            s = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            message.setJStr(s);
            return message;
        }

    }

    @Override
    public Message getAuthor(String toUserId, String fromUserId,ObjectMapper objectMapper) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        UserDaoImp  userDaoImp = new UserDaoImp();
        User user = userDaoImp.getUserInformation(toUserId,connection,message);
        user.setFollowNum(userDaoImp.getFollowNum(connection,toUserId,message));
        user.setFansNum(userDaoImp.getFansNum(connection,toUserId,message));
        user.setCollectionNum(userDaoImp.getCollectionNum(connection,toUserId,message));
        user.setFlag(userDaoImp.judgeFollow(connection,toUserId,fromUserId));
        String s = null;
        try {
            s = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            message.setJStr(s);
            return message;
        }

    }

    @Override
    public Message operateFollow(String userId, String authorId, int flag) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        UserDaoImp userDaoImp = new UserDaoImp();
        Boolean aBoolean = userDaoImp.operateFollow(connection, flag, authorId, userId);
        if (aBoolean) {
            message.setStatue("200");
        } else {
            message.setStatue("500");
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;

    }

    @Override
    public Message changeUserInfo(String userId, String userName) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        UserDaoImp userDaoImp = new UserDaoImp();
        message.setStatue(userDaoImp.changeUser(connection, userName, userId));
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return message;
        }

    }

    @Override
    public Message searchUser(String keyword) {
        Message message = new Message();
        Connection connection = DataBaseUtils.getConnection();
        UserDaoImp userDaoImp = new UserDaoImp();
        List<User> users = userDaoImp.searchUsers(connection,keyword);
        try {
            message.setJStr(new ObjectMapper().writeValueAsString(users));
            message.setStatue("200");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @Override
    public Message deleteUser(String userId) {
        Message message = new Message();
        UserDaoImp userDaoImp = new UserDaoImp();
        Connection connection = DataBaseUtils.getConnection();
        String statue = userDaoImp.deleteUser(connection,userId);
        message.setStatue(statue);
        return message;
    }
}
