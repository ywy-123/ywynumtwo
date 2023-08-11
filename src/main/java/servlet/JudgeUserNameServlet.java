package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;
import utils.DataBaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/judgeUserName")
public class JudgeUserNameServlet extends HttpServlet {
    public static final String judgeUserNameExists="select user_id from users where user_name=?";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.得到前端的参数
        String username = req.getParameter("username");//与register.js里的？后username保持一致
        //2.根据参数：查数据库
        //2.1数据库查询
        Connection connection = DataBaseUtils.getConnection();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        //2.2
        Message message = new Message();
        try {
            preparedStatement = connection.prepareStatement(judgeUserNameExists);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                message.setStatue("300");
                message.setDescription("用户已存在");
            }else{
                message.setStatue("200");//表示用户名成功
                message.setDescription("");
            }
        } catch (SQLException e) {
            message.setStatue("500");//出错
            message.setDescription("服务器出错");
            e.printStackTrace();
        }finally {
            //3.响应（得到响应的数据）
            resp.getWriter().write(new ObjectMapper().writeValueAsString(message));
            DataBaseUtils.closeSource(connection,preparedStatement,resultSet);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}