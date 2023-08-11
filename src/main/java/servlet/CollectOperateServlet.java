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

@WebServlet("/afterLogin/collectOperate")
public class CollectOperateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String articleId = req.getParameter("articleId");
        Boolean isCollected = Boolean.parseBoolean(req.getParameter("isCollected"));
//        System.out.println("isCollected:"+isCollected);
        ObjectMapper om = new ObjectMapper();
        ArticleService articleService = new ArticleService();
        Message message = articleService.collectOperate(articleId,isCollected,(String) req.getSession().getAttribute("userId"));
        resp.getWriter().write(om.writeValueAsString(message));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}