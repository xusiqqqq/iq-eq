package xsj.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import xsj.common.R;
import xsj.entity.User;
import xsj.service.UserService;
import xsj.utils.VeDate;
import xsj.utils.VerifyCodeUtil;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //获取验证码
    @ResponseBody
    @RequestMapping(value = "/code",method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response)  {
        HttpSession session = request.getSession();
        VerifyCodeUtil verifyCode=new VerifyCodeUtil();
        try(OutputStream out = response.getOutputStream()) {
            VerifyCodeUtil.output(verifyCode.getImage(),out );
            session.setAttribute("code",verifyCode.getText());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value="/verify",method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    public String verifyUsername(Model model,String userName){
        if(userName==null||userName.equals("")) return "用户名不能为空";
        LambdaQueryWrapper<User> qw=new LambdaQueryWrapper<>();
        qw.eq(User::getUserName,userName);
        User user = this.userService.getOne(qw);
        if(user!=null) return "该用户名已经被注册";
        return "用户名未被注册";
    }

    @RequestMapping(value="/toLogin",method =RequestMethod.GET)
    public String toLogin(){
        return "/user/login";
    }

    //登录功能
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login (Model model, HttpServletRequest request, String username,
                          String password,String code){
        HttpSession session=request.getSession();
        String correntCode =String.valueOf(session.getAttribute("code")).toLowerCase();
        System.out.println(code);
        System.out.println(correntCode);
        if(!correntCode.equals(code.toLowerCase())){
            model.addAttribute("message","验证码错误");
            return "/user/login";
        }
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user=userService.getOne(queryWrapper);
        System.out.println(username+password+code);

        if(user==null){
            model.addAttribute("message", "登陆失败，用户名不存在");
            return "/user/login";
        }
        if(!user.getPassword().equals(password)){
            model.addAttribute("message", "登录失败，密码错误");
            return "/user/login";
        }
        if(user.getStatus()==1) {
            model.addAttribute("message", "登录失败，账号已禁用");
            return "/user/login";
        }
        //更新用户最后登录时间
        user.setLastLoginDate(LocalDateTime.now());
        this.userService.saveOrUpdate(user);
        session.setAttribute("LOGIN_USER",user);
        return "redirect:/book/toList";
    }

    @RequestMapping(value = "/toRegister")
    public String toRegister(){
        return "/user/register";
    }

    //注册
    @RequestMapping(value = "/register" ,method = RequestMethod.GET)
    public String register(Model model,String userName,String password,String pwd2){
        if(!password.equals(pwd2)){
            model.addAttribute("message","两次密码不一致");
            return "/user/register";
        }
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User user = userService.getOne(queryWrapper);
        if(user!=null){
            model.addAttribute("message","该用户名已经被注册");
            return "/user/register";
        }
        User user1=new User();
        user1.setUserName(userName);
        user1.setPassword(password);
        user1.setStatus(0);
        user1.setCreateDate(LocalDateTime.now());
        this.userService.saveOrUpdate(user1);
        model.addAttribute("message","账号注册成功，请登录");
        return "/user/login";
    }


    //退出登录
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("LOGIN_USER");
        return "/user/login";
    }

}
