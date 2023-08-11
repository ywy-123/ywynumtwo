package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

//注册
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    public static final String InsertUserSQl="";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //在servlet生成user_id
        String userId = UUID.randomUUID().toString();//随机生成user_id
        ObjectMapper objectMapper = new ObjectMapper();
        Message message =null;
        PrintWriter writer = resp.getWriter();
        if(username==null||!username.matches("\\w{2,10}")){
            message=new Message();
            message.setStatue("n");
            writer.write(objectMapper.writeValueAsString(message));//用户名不符合规范
            return;
        }
        if(password==null||!password.matches("\\w{6,20}")){
            message = new Message();
            message.setStatue("p");
            writer.write(objectMapper.writeValueAsString(message));//密码不规范
            return;
        }
       //调用service层
        UserService userService = new UserService();
       message=userService.register(username,password,userId);//接受
        //响应
        writer.write(objectMapper.writeValueAsString(message));//将message对象写到前端resp.data




    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}