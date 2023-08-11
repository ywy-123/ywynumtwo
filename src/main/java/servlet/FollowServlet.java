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

@WebServlet("/afterLogin/follow")
public class FollowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String toUserId = req.getParameter("toUserId");
        Integer isFollow = Integer.parseInt(req.getParameter("isFollow"));
        String userId = (String) req.getSession().getAttribute("userId");
        ObjectMapper objectMapper = new ObjectMapper();
        UserService userService = new UserService();
        Message message = userService.operateFollow(userId, toUserId, isFollow);
        String s = objectMapper.writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}