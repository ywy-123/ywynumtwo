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

@WebServlet("/afterLogin/saveUserInfor")
public class SaveUserInforServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("updateName");
        String userId = (String)req.getSession().getAttribute("userId");
        UserService userService = new UserService();
        Message message = userService.changeUserInfo(userId, username);
        //将message返回到前端
        String s = new ObjectMapper().writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}