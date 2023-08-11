package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Article;
import pojo.Message;
import service.ArticleService;
import service.UserService;

import java.io.IOException;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1代表查询用户，2代表查询文章
        Integer instance = Integer.parseInt(req.getParameter("instance"));
        String keyword = req.getParameter("keyword");
        Message message = null;
        if(instance==1){
            UserService userService = new UserService();
            message = userService.searchUser(keyword);
        }else{
            ArticleService articleService = new ArticleService();
            message = articleService.searchArticles(keyword);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}