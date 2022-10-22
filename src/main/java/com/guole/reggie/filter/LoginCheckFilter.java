package com.guole.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.guole.reggie.common.BaseContext;
import com.guole.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 检查用户是否登录
 * @author 郭乐
 * @date 2022/10/14
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    /**
     * 路径匹配器,支持通配符
     * 路径匹配器
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
       //获取请求url
        String url = request.getRequestURI();
        log.info("拦截到的请求为：{}",url);

        //定义不需要处理的uri
        String[] arrs = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                /*"/resources/**",*/
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login",//移动端登录
                "/front/**"
                
        };
        boolean check = check(arrs, url);
        //不需要处理，直接放行
        if (check == true) {
            log.info("放行的路径为{},此路径不需要拦截",url);
            filterChain.doFilter(request,response);
            return;
        }
        //用户已经登录，则放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已经登录，登录用户的ID为：{}",request.getSession().getAttribute("employee"));

            //获得线程id
            long id = Thread.currentThread().getId();
            log.info("在filter loginCheckFilter 类中 线程id为：{}",id);
            //将用户id存入到ThreadLocal域中
            Long userId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        //移动端用户已经登录，则放行
        if (request.getSession().getAttribute("user") != null){
            log.info("移动端用户已经登录，的ID为：{}",request.getSession().getAttribute("user"));


            //将用户id存入到ThreadLocal域中
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        //用户未登录，通过输出流向客户端响应
        log.info("用户未登录，已经被拦截");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



/*        //输出日志
        log.info("拦截到的请求路径为：{}",request.getRequestURL());
        //放行
        filterChain.doFilter(request,response);*/
    }

    public boolean check(String[] arrs,String url){
        for (String arr :
                arrs) {
            boolean match = PATH_MATCHER.match(arr, url);
            if (match == true) {
                return true;
            }
        }
        return false;
    }
}
