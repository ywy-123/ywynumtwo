package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ArticleDaoImp;
import dao.BaseDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import pojo.Article;
import pojo.Message;
import utils.DataBaseUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;

@WebServlet("/afterLogin/changeText")
@MultipartConfig
public class ChangeTextServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String articleId = req.getParameter("articleId");
        Part content = req.getPart("content");
        InputStream input = content.getInputStream();
        byte[] data = new byte[input.available()];

        input.read(data);
        input.close();
        String articleContent = new String(data,"utf-8");
//        System.out.println("更改完毕");


        Connection connection = DataBaseUtils.getConnection();
        String updateArticle = "update articles set content = ? where article_id=?";
        int rowNum = new ArticleDaoImp().update(connection,updateArticle,articleContent,articleId);
        Message message = null;
        ObjectMapper om = new ObjectMapper();
        if(rowNum ==1){
            message = new Message("200");
        }else{
            message = new Message("500");
        }
        String s = om.writeValueAsString(message);
        resp.getWriter().write(s);
    }
}