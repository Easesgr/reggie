package com.anyi.reggie.filter;

import cn.hutool.json.JSONUtil;
import com.anyi.reggie.common.R;
import com.anyi.reggie.common.UserContext;
import com.anyi.reggie.entity.Employee;
import com.anyi.reggie.entity.User;
import com.sun.prism.impl.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 安逸i
 * @version 1.0
 */
@WebFilter(filterName = "userFilter",urlPatterns = "/*")
@Slf4j
public class UserFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        log.info(servletRequest.getRequestURI());
        // 设置不要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/layout",
                "/backend/**",
                "/front/**",
                "/swagger-ui.html",
                "/user/code",
                "/user/login"
        };
        if (match(servletRequest.getRequestURI(),urls)){
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        // 需要验证的请求
        User user = (User)servletRequest.getSession().getAttribute("user");
        Employee employee = (Employee)servletRequest.getSession().getAttribute("employee");

        // 需要验证的请求
        if (user != null){
            UserContext.setUserId(user.getId());
            chain.doFilter(servletRequest,servletResponse);
        }else if (employee !=null){
            UserContext.setUserId(employee.getId());
            chain.doFilter(servletRequest,servletResponse);
        }else {
            response.getWriter().write(JSONUtil.toJsonStr(R.error("NOTLOGIN")));

        }
    }

    // 匹配url
    public boolean match(String url,String[] urls){
        for (String item : urls) {
            if (PATH_MATCHER.match(item, url)){
                return true;
            }
        }
        return false;
    }
}
