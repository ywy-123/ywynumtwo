package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import pojo.Message;
import service.ArticleService;

import java.io.*;
import java.util.UUID;

@WebServlet("/afterLogin/filePublished")
@MultipartConfig
public class ArticlePublishServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post执行失败");
        super.doGet(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("执行到post");
        Part content = req.getPart("file");//内容

//        System.out.println("part get");

        String Ptitle = req.getParameter("title");//标题

//        System.out.println("title get");

        String category = req.getParameter("categories");//标签

//        System.out.println("category:"+category);

        String[] split = category.split(",");
        String userid = (String) req.getSession().getAttribute("userId");//根据id在storage里面创建文件夹

        int[] categories = new int[split.length-1];

        for(int i=1;i<split.length;i++){
//            System.out.println("转换前："+split[i]);
            categories[i-1]=Integer.parseInt(split[i]);
//            System.out.println("转换后:"+categories[i-1]);
        }

        String articleId = UUID.randomUUID().toString();

        Message message=null;
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("dian3");
        //插入数据库
        ArticleService articleService = new ArticleService();
        message=articleService.publishArticle(userid,articleId,Ptitle,categories,content.getInputStream());

        String s = objectMapper.writeValueAsString(message);
        resp.getWriter().write(s);
    }
}