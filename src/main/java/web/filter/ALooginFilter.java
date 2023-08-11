package web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.Message;

import java.io.IOException;

@WebFilter(urlPatterns = {"/afterLogin/*","/html/*"})
public class ALooginFilter extends HttpFilter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {

    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("login filter");
        String userId = (String)req.getSession().getAttribute("userId");
        System.out.println(userId);
        if(userId==null){
            Message message = new Message();
            message.setStatue("401");
            res.getWriter().write(new ObjectMapper().writeValueAsString(message));
            System.out.println("重定向");//
            return ;
        }
        chain.doFilter(req,res);
    }
}
