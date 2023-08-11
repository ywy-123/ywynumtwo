package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;

import java.io.IOException;

@WebServlet("/judgeLogin")
public class JudgeLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object userId = req.getSession().getAttribute("userId");
        Message message =new Message();
        ObjectMapper om=new ObjectMapper();
        if(userId==null){
            message.setValue("0");
        }else{
            message.setValue("1");
        }
        String messageStr = om.writeValueAsString(message);
        resp.getWriter().write(messageStr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}