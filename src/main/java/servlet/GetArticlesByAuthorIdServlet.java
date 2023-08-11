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

import java.io.IOException;

@WebServlet("/getArticlesByUserId")
public class GetArticlesByAuthorIdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorId = req.getParameter("userId");
        Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
        ObjectMapper objectMapper = new ObjectMapper();
        ArticleService articleService = new ArticleService();
        Message message = articleService.getArticlesByAuthorId(authorId,pageNum,objectMapper);
        String s = objectMapper.writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}