package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;
import service.UserService;
import utils.DataBaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/getAuthorInformation")
public class GetAuthorInformation extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        String authorName = req.getParameter("authorName");
        String authorId=  nameToId(authorName);
        String userId = (String)req.getSession().getAttribute("userId");
        Message usermessage = new UserService().getAuthor(authorId,userId, objectMapper);
        String s = objectMapper.writeValueAsString(usermessage);//转出json字符串发到前端
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }





    public static final String NAMETOIDSQL = "select user_id from users where user_name=?";
    public String nameToId(String userName){
        Connection connection = DataBaseUtils.getConnection();
        PreparedStatement preparedStatement;
        String id = null;
        try {
            preparedStatement = connection.prepareStatement(NAMETOIDSQL);
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                 id = resultSet.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return id;
        }


    }
}