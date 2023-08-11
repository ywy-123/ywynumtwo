package web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebFilter;
import pojo.Article;
import utils.DataBaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


@WebFilter(urlPatterns = "/articleDetails.html")
public class AArticleFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException {
        String queryString = req.getQueryString();
        if(judgeIsFreezing(queryString)){
            try {
                chain.doFilter(req,res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                res.sendRedirect("http://localhost:8080/ArticlePublishingPlatform/index.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static final String judgeIsFreezing="select is_freezing from articles where article_id=?";
    public Boolean judgeIsFreezing(String articleId){
        Connection connection = DataBaseUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(judgeIsFreezing);
            preparedStatement.setString(1,articleId);
            rs = preparedStatement.executeQuery();
            rs.next();
            return rs.getInt("is_freezing")==1 ? false : true ;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}