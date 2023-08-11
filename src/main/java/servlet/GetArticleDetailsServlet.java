package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;
import service.ArticleService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@WebServlet("/getArticleDetail")
public class GetArticleDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String articleId = req.getParameter("articleId");
        ObjectMapper om = new ObjectMapper();
        Message message;
        ArticleService articleService = new ArticleService();
        message = articleService.getArticleDetails(om,articleId,(String)req.getSession().getAttribute("userId"));
        String s = om.writeValueAsString(message);
        resp.getWriter().write(s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}