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

/*
搜索获得文章列表->不需要登录与权限
 */
@WebServlet("/getArticles")
public class GetArticles extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer num =Integer.parseInt(req.getParameter("pageNum"));
        Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = new ArticleService().getAllArticles(objectMapper,num,categoryId);
        //调用service层的接口方法，由service层去调用dao层，在dao层中生成一个list返回到这个servlet里面
        String messageJson = objectMapper.writeValueAsString(message);
        System.out.println(messageJson);
        resp.getWriter().write(messageJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
