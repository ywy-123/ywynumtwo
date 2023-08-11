package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pojo.Message;
import utils.DataBaseUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    //public static final String selectSql="select users.user_id,users.password,users.is_freezing,user_role.role_id " +
    //        "from users,user_role where users.user_name=? AND users.user_id=user_role.user_id";
    //笛卡尔积
    public static final String select = "select users.user_id,users.password,users.is_freezing,user_role.role_id " +
            "from users inner join user_role on users.user_id=user_role.user_id where users.user_name=?";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //System.out.println("访问到login");
        ObjectMapper om = new ObjectMapper();//转Json
        Message message = new Message();
        PrintWriter writer = resp.getWriter();
        if(username==null||!username.matches("\\w{2,10}")){
            message.setStatue("n");
            writer.write(om.writeValueAsString(message));//用户名不符合规范
            return;
        }
        if(password==null||!password.matches("\\w{6,20}")){
            message.setStatue("p");
            writer.write(om.writeValueAsString(message));//密码不规范
            return;
        }

        Connection connection = DataBaseUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setString(1,username);//Z设置占位符
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                if(resultSet.getString("password").equals(password)){
                    if(resultSet.getInt("is_freezing")==0){
                        HttpSession session = req.getSession();
                        session.setAttribute("username",username);
                        session.setAttribute("userId",resultSet.getString("user_id"));
                        Integer role = resultSet.getInt("role_id");
                        session.setAttribute("role",role);
                        message.setStatue("3");
                        message.setValue(role+"");
                    }else{
                        message.setStatue("2");
                    }
                }else {
                    message.setStatue("1");
                }
            }else{
                message.setStatue("0");
            }
            writer.write(om.writeValueAsString(message));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}