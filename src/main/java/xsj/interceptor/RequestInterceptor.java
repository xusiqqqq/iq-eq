package xsj.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xsj.entity.RequestLog;
import xsj.entity.User;
import xsj.service.RequestLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    private RequestLogService requestLogService;
    private NamedThreadLocal<Long> timeThreadLocal=new NamedThreadLocal<>("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        Long startTime=System.currentTimeMillis();
        timeThreadLocal.set(startTime);
        HttpSession session=request.getSession(false);
        RequestLog requestLog=new RequestLog();
        if(session==null){
            requestLog.setRequestPromoter("未登录");
        }else{
            if(session.getAttribute(("LOGIN_USER"))!=null){
                User user = (User) session.getAttribute("LOGIN_USER");
                requestLog.setRequestPromoter(user.getUserName());
            }else{
                requestLog.setRequestPromoter("未登录");
            }
        }
        requestLog.setRequestUrl(request.getRequestURI());
        requestLog.setRequestDate(LocalDateTime.now());
        request.setAttribute("requestLog",requestLog);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long endTime=System.currentTimeMillis();
        Long startTime=timeThreadLocal.get();
        Long time=endTime-startTime;
        RequestLog requestLog = (RequestLog) request.getAttribute("requestLog");
        requestLog.setRequestDuration(time);
        this.requestLogService.save(requestLog);
    }

}
