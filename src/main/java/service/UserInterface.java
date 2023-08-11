package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.Message;

public interface UserInterface {
    //注册时的
    Message register(String username,String password,String userId);
    Message getUser(String userId, ObjectMapper objectMapper);
    Message getAuthor(String toUserId,String fromUserId,ObjectMapper objectMapper);
    Message operateFollow(String userId,String authorId,int flag);
    Message changeUserInfo(String userId,String userName);

    Message searchUser(String keyword);

    Message deleteUser(String userId);
}
