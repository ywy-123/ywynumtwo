package web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebFilter;
import pojo.Message;

import java.io.IOException;

@WebFilter("/afterLogin/manager/*")
public class BManagerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Integer role = (Integer) req.getSession().getAttribute("role");
        System.out.println("manager filter");
        if(role==2){
            chain.doFilter(req,res);
        }else{
            res.getWriter().write(new ObjectMapper().writeValueAsString(new Message("401")));
        }
    }
}