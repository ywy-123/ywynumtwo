package servlet;

import dao.ArticleDaoImp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DataBaseUtils;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;

@WebServlet("/afterLogin/manager/getArticleTotalNum")
public class getArticleTotalNumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = DataBaseUtils.getConnection();
        Statement statement=null;
        Integer num = 0;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ArticleDaoImp.GETARTICLESNUM);
            resultSet.next();
            num = resultSet.getInt("totalNum");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter().write(num+"");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}