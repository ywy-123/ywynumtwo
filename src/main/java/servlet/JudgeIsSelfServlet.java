package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/afterLogin/judgeIsSelf")
public class JudgeIsSelfServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String)req.getSession().getAttribute("userId");
        String authorId = req.getParameter("authorId");
        PrintWriter writer = resp.getWriter();
        if(userId==null){
            writer.write("2");
        }else if(authorId.equals(userId)){
            writer.write("1");
        }else{
            writer.write("0");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}