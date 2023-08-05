package com.bear.filter;

import com.alibaba.fastjson.JSON;
import com.bear.common.BaseContext;
import com.bear.common.Result;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //use this path matcher to support wildcards**
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        String[] urls = {"/employee/login", "/employee/logout", "/backend/**", "/front/**", "/common/**",
                "/user/login", "/user/sendMsg"};

        // check match or not
        boolean check = check(urls, requestURI);

        if (check) {
            log.info("Request url:{}, no need to intercept", requestURI);
            filterChain.doFilter(request, response); // pass
        } else if (request.getSession().getAttribute("employee") != null) {
            log.info("User is logged in, id is {}", request.getSession().getAttribute("employee"));

            long threadID = Thread.currentThread().getId();
            log.info("current thread id is {}", threadID);

            Long empID = (Long) request.getSession().getAttribute("employee");
            //set id to ThreadLocal by call this static utility class
            BaseContext.setCurrentID(empID);


            filterChain.doFilter(request, response); // if logged, then pass
        } else if (request.getSession().getAttribute("user") != null) {// check user login or not
            log.info("User is logged in, id is {}", request.getSession().getAttribute("user"));

            Long userID = (Long) request.getSession().getAttribute("user");
            //set id to ThreadLocal by call this static utility class
            BaseContext.setCurrentID(userID);

            filterChain.doFilter(request, response); // if logged, then pass
        } else {
            log.info("User not logged in");
            // if not logged in, then response the json result data by outputStream
            response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        }



    }

    public boolean check(String[] urls, String requestURI){
        for(String url: urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}