package com.cqfangxin.web;

import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.domain.Result;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.UserService;
import com.cqfangxin.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/admin/login", method= RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestBody LoginBean loginUser, HttpSession session){
        User admin = userService.getUser(loginUser);
        if(admin != null){
            session.setAttribute(Constant.USERINFO, admin);
            session.setMaxInactiveInterval(60000000);
            return new Result("success", Constant.LOGIN_SUCCESS, admin);
        }else{
            return new Result("fail", Constant.LOGIN_FAIL);
        }
    }





}
