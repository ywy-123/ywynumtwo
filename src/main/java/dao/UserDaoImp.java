package dao;

import pojo.Article;
import pojo.Message;
import pojo.User;
import utils.DataBaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImp extends BaseDao<User> implements UserDao{

    @Override
    public List<User> getManagerIndex(Connection connection) {
        return null;
    }

    //BaseDao baseDao = new BaseDao() ;
    //UserDaoImp userDaoImp=new UserDaoImp();
    @Override
    public Message register(String username, String password, String userId) {
        Message message = new Message();
        boolean flag = false;
        System.out.println("建立连接");
        Connection connection = DataBaseUtils.getConnection();
        System.out.println("链接建立成功");
        try {
            connection.setAutoCommit(false);//关闭自动提交
            //操作数据库
//            flag = userDaoImp.insertUsers(connection,username,password,userId);
            flag = insertUsers(connection,username,password,userId);
           if(flag){
//               flag = userDaoImp.insertUserRole(connection,userId);
               flag = insertUserRole(connection,userId);
               if(flag) {
                   connection.commit();//提交
                   System.out.println("提交成功！！！！");
                   message.setStatue("200");//注册（插入）成功
               }else {
                   try {
                       connection.rollback();//回滚
                   } catch (SQLException ex) {
                       ex.printStackTrace();
                   }
                   message.setStatue("500");//注册（插入）失败
               }
           }else{
               try {
                   connection.rollback();//回滚
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
               message.setStatue("500");//注册（插入）失败
           }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();//回滚
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            message.setStatue("500");//出错
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }
    }

    @Override
    public List<User> getAllUsers() {
        Connection connection = DataBaseUtils.getConnection();
        List<User> users = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from users where is_freezing=0";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("UserId"));
                user.setUserName(rs.getString("UserName"));
                user.setCreateTime(rs.getString("CreateTime"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //指定用户角色，插入user_role
    public static final String INSERTUSERROLE="insert into user_role(role_id,user_id) value(1,?)";
    public  boolean insertUserRole(Connection connection,String userId){
        PreparedStatement preparedStatement = null;
        boolean flag = false;
        try {
            preparedStatement=connection.prepareStatement(INSERTUSERROLE);
            //1.给preparedStatement赋值
            preparedStatement.setString(1,userId);
            //2.执行sql语句
            preparedStatement.executeUpdate();
            //返回值
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;//如果出现异常，就设置为false
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return flag;
        }

    }


    public static final String INSERTUSER = "insert into users(user_id,user_name,password) value(?,?,?)";
    public  boolean insertUsers(Connection connection, String username, String password, String userId){
        System.out.println("insertUser执行");
        PreparedStatement preparedStatement = null;
        boolean flag = false;
        try {
            preparedStatement = connection.prepareStatement(INSERTUSER);
            //1.给preparedStatement赋值
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,password);
            System.out.println("测试专用");
            //2.执行sql语句
            preparedStatement.executeUpdate();
            System.out.println("执行完update了");
            //返回值
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;//如果出现异常，就设置为false
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return flag;
        }

    }

    public static final String GETSQL = "select user_id,user_name,create_time from users where user_id=?";
    @Override
    public User getUserInformation(String userId ,Connection connection,Message message) {
        User user = new User();
        PreparedStatement preparedStatement=null;

        try {
            preparedStatement = connection.prepareStatement(GETSQL);
            preparedStatement.setString(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getString("user_id"));
                user.setUserName(resultSet.getString("user_name"));
                user.setCreateTime(resultSet.getString("create_time"));
                message.setStatue("200");
            }else{
                message.setStatue("500");
                message.setDescription("用户id不存在！！！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }


    }
    public static final String GETFOLLOWNUMS = "select count(*) as FollowNums from follow where from_userId =?";
    @Override
    public Integer getFollowNum(Connection connection, String userId, Message message) {
        PreparedStatement preparedStatement=null;
        Integer num1 = null;
        try {
             preparedStatement = connection.prepareStatement(GETFOLLOWNUMS);
             preparedStatement.setString(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
            num1 = resultSet.getInt("FollowNums");
            message.setStatue("200");}
            else{
                message.setDescription("用户id不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return num1;
        }

    }
    public static final String GETFANSNUMS = "select count(*) as getFansNum from follow where to_userId =?";

    @Override
    public Integer getFansNum(Connection connection, String userId, Message message) {

        PreparedStatement preparedStatement=null;
        Integer num1 = null;
        try {
            preparedStatement=connection.prepareStatement(GETFANSNUMS);
            preparedStatement.setString(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                num1 = resultSet.getInt("getFansNum");
                message.setStatue("200");
            }else{
                message.setStatue("500");
                message.setDescription("用户id不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return num1;
        }
    }


    public static final String GETCollectionNUMS = "select count(*) as getCollectionsNum from collect where user_id =?";

    @Override
    public Integer getCollectionNum(Connection connection, String userId, Message message) {


        PreparedStatement preparedStatement=null;
        Integer num1 = null;
        try {
            preparedStatement=connection.prepareStatement(GETCollectionNUMS);
            preparedStatement.setString(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                num1 = resultSet.getInt("getCollectionsNum");
                message.setStatue("200");
            } else{
                message.setDescription("用户id不存在");
                message.setStatue("500");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return num1;
        }
    }

    public static  final String SELECTJUDGE="select id from follow where from_userId=? and to_userId=?";
    @Override
    public Integer judgeFollow(Connection connection, String authorId, String userId) {
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement = connection.prepareStatement(SELECTJUDGE);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return 1;//已有数据（已关注）
            }else{
                return 2;//没有数据（未关注）
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return 0;//报错
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
public static final String INSERTFOLLOW="insert into follow (from_userId,to_userId) values(?,?) ";
public static final String DELETEFOLLOW="delete from follow where from_userId=? and to_userId=?";

    @Override
    public Boolean operateFollow(Connection connection, int flag, String authorId, String userId) {
        int num=0;
        if(flag==1){
            num = update(connection,DELETEFOLLOW,userId,authorId);
        }else if(flag==2){
            num=update(connection,INSERTFOLLOW,userId,authorId);
        }
        if(num!=0){
            return true;
        }else {
            return false;
        }
    }
public static final String CHANGEUSER="update users set user_name=? where user_id=?";
    @Override
    public String changeUser(Connection connection, String username ,String userid) {
        int num=0;//在调用baseDao时，返回值是影响的数据行数，如果为0，就是失败
        num=update(connection,CHANGEUSER,username,userid);
        if(num!=0){
            return "200";//返回正确码
        }
        return "500";
    }


    public static final String searchUsers = "select user_id,user_name,is_freezing,create_time from users where user_name like ?";
    @Override
    public List<User> searchUsers(Connection connection, String keyword) {
        keyword = "%"+keyword+"%";
        PreparedStatement preparedStatement = null;
        List<User> list = new ArrayList<>();
        ResultSet rs = null;
        try{
            preparedStatement = connection.prepareStatement(searchUsers);
            preparedStatement.setString(1,keyword);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setCreateTime(rs.getDate("create_time").toString());
                user.setIsFreezing(rs.getInt("is_freezing"));
                list.add(user);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }
    }


    public static final String deleteAboutUser="delete users,user_role from users inner join user_role on users.user_id=user_role.user_id where users.user_id=?";
    public static final String deleteUsersArticle="delete articles,user_article,article_category from articles inner join user_article on articles.article_id=user_article.article_id inner join article_category on article_category.article_id=articles.article_id where user_article.user_id=?";
    public static final String deleteCollect = "delete from collect where author_id=? OR user_id=?";
    public static final String deletefollow = "delete from follow where from_userId=? OR to_userId=?";
    @Override
    public String deleteUser(Connection connection, String userId) {
        PreparedStatement preparedStatement =null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(deleteAboutUser);
            preparedStatement.setString(1,userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(deleteUsersArticle);
            preparedStatement.setString(1,userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(deleteCollect);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(deletefollow);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
            return "200";
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return "500";
        }
    }
}

    /*针对其他问题的实现*/



