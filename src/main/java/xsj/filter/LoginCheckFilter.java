package xsj.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher matcher=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        /**
         * 1、获取本次请求uri
         * 2、判断是否需要处理该请求
         * 3、如果不需要处理，则直接放行
         * 4、判断登录状态，如果已登录，则直接放行
         * 5、如果未登录则返回登陆结果
         */
        String uri=request.getRequestURI();
        String[] uris=new String[]{
                "/user/toLogin",
                "/user/logout",
                "/user/login",
                "/user/toRegister",
                "/user/register",
                "/user/code",
                "/static/**",
                "/templates/**",
                "/user/verify"
        };
        if(check(uris,uri)){
            filterChain.doFilter(request,response);
            return;//结束后面的语句
        }
        if(request.getSession().getAttribute("LOGIN_USER")!=null){
            filterChain.doFilter(request,response);
            return;//结束后面的语句
        }
        response.sendRedirect("/user/toLogin");
    }


    public boolean check(String[] uris,String requestUri){
        for (String uri : uris) {
            if(matcher.match(uri,requestUri))return true;
        }
        return false;
    }
}
