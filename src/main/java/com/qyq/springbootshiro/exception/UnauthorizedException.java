package com.qyq.springbootshiro.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class UnauthorizedException {

    @ExceptionHandler(value = org.apache.shiro.authz.UnauthorizedException.class)
    @ResponseBody
    public String Unauthorized(HttpServletRequest request, Exception e){
        return "你没有此操作权限！";
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseBody
    public String Unauthenticated(HttpServletRequest request, Exception e){
        return "你没有此访问权限！";
    }
}
