package xsj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import xsj.entity.RequestLog;
import xsj.service.RequestLogService;

import java.util.List;

@Controller
@RequestMapping("/log")
public class RequestLogController {
    @Autowired
    private RequestLogService requestLogService;

    //分页显示所有的请求记录
    @RequestMapping("/toLogList")
    public String toList(Model model,Integer currentPage,String search){
        if(currentPage==null){
            currentPage=1;
        }
        Page<RequestLog> pageInfo=new Page<>(currentPage,15);
        LambdaQueryWrapper<RequestLog> qw=new LambdaQueryWrapper<>();
        qw.orderByDesc(RequestLog::getRequestDate);
        Page<RequestLog> page = this.requestLogService.page(pageInfo, qw);
        model.addAttribute("search",search);
        model.addAttribute("PAGE",page);
        model.addAttribute("LOG_LIST",page.getRecords());
        model.addAttribute("uri","toLogList");
        return "/requestLog/logList";
    }

    //删除所选请求
    @ResponseBody
    @RequestMapping(value = "/deleteSelect.do",method = RequestMethod.POST,produces = "application/json")
    public String delete(@RequestBody List<Integer> ids){
        boolean b = this.requestLogService.removeByIds(ids);
        if(!b) return "批量删除失败";
        return "批量删除成功";
    }

    //模糊搜索
    @RequestMapping(value = "/search.do",method =RequestMethod.GET )
    public String serachBook(Model model,Integer currentPage,String search){
        if(currentPage==null){
            currentPage=1;
        }
        Page<RequestLog> pageInfo=new Page<>(currentPage,15);
        LambdaQueryWrapper<RequestLog> qw=new LambdaQueryWrapper<>();
        qw.like(RequestLog::getRequestUrl,search)
                .or().like(RequestLog::getRequestPromoter,search)
                .or().like(RequestLog::getRequestDate,search)
                .or().like(RequestLog::getRequestDuration,search);
        qw.orderByDesc(RequestLog::getRequestDate);
        Page<RequestLog> page = this.requestLogService.page(pageInfo, qw);
        System.out.println();
        model.addAttribute("search",search);
        model.addAttribute("PAGE",page);
        model.addAttribute("LOG_LIST",page.getRecords());
        model.addAttribute("uri","search.do");
        return "/requestLog/logList";
    }


}
