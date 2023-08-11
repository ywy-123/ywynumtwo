package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataBaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dao.ArticleDaoImp.onePageNum;

@WebServlet("/getPageNumByAuthorId")
public class GetArticlesNumByAuthorIdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorId = req.getParameter("userId");
        Connection connection = DataBaseUtils.getConnection();
        String getPageNumByAuthorId = "select count(*) as num from user_article where user_id=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(getPageNumByAuthorId);
            preparedStatement.setString(1,authorId);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            resp.getWriter().write(Math.ceil((double)resultSet.getInt("num")/onePageNum)+"");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtils.closeSource(connection,preparedStatement,resultSet);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}