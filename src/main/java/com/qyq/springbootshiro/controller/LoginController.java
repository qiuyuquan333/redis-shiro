package com.qyq.springbootshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/userlogin")
    public String UserLogin(){
        return "userlogin.html";
    }

    @RequestMapping("/adminlogin")
    public String AdminLogin(){
        return "adminlogin.html";
    }

    @RequestMapping("/index")
    public String IndexController(){
        return "index.html";
    }

    @RequestMapping("/unauthorized")
    @ResponseBody
    public String UnaController(){
        return "你没有访问权限";
    }

    @RequiresPermissions("add")
    @RequestMapping("/add")
    public String AddController(){
        return "add.html";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String LogoutController(HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        session.invalidate();
        subject.logout();
        return "退出";
    }

    @RequestMapping("/adologin")
    public String AdminDoLogin(@RequestParam("uname") String name, @RequestParam("upassword") String password){
        UsernamePasswordToken token = new UsernamePasswordToken(name,password,"admin");
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "/index";
        } catch (Exception e) {
            e.printStackTrace();
            return "/adminlogin";
        }
    }

    @RequestMapping("/udologin")
    public String UserDoLogin(@RequestParam("uname") String name, @RequestParam("upassword") String password){
        UsernamePasswordToken token = new UsernamePasswordToken(name,password,"user");
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "/index";
        } catch (Exception e) {
            e.printStackTrace();
            return "/userlogin";
        }
    }
}
