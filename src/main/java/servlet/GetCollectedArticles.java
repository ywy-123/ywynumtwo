package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;
import service.ArticleService;
import java.io.IOException;

@WebServlet("/afterLogin/getCollectedArticles")
public class GetCollectedArticles extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        Integer category = Integer.parseInt(req.getParameter("category"));
        Integer pageNum =  Integer.parseInt(req.getParameter("pageNum"));
        ObjectMapper om = new ObjectMapper();
        ArticleService articleService = new ArticleService();
        Message message = articleService.getCollectedArticles(userId,pageNum,category);
        String s = om.writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}