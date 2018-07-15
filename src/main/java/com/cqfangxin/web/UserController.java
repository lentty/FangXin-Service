package com.cqfangxin.web;

import com.cqfangxin.domain.Result;
import com.cqfangxin.domain.TableDataResult;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.UserService;
import com.cqfangxin.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Result getUsers(@PathVariable("type") Integer type, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            List<User> admins = userService.getUsers(type);
            TableDataResult resultObj = new TableDataResult();
            resultObj.setTotalCount(admins.size());
            resultObj.setData(admins);
            return new Result("success", Constant.DEAL_SUCCESS, resultObj);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserById(@PathVariable("id")Integer userId, HttpSession session) {
        User loginUser = (User) session.getAttribute(Constant.USERINFO);
        if (loginUser == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            User user = userService.getUserById(userId);
            return new Result("success", Constant.DEAL_SUCCESS, user);
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
        public Result editUser(@RequestBody User user, HttpSession session) {
        User loginUser = (User) session.getAttribute(Constant.USERINFO);
        if (loginUser == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = userService.editUser(user, loginUser.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteUserById(@PathVariable("id") Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = userService.deleteUserById(id);
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Result getLoginUserInfo(HttpSession session) {
        User loginUser = (User) session.getAttribute(Constant.USERINFO);
        if (loginUser == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            User user = userService.getUserById(loginUser.getId());
            return new Result("success", Constant.DEAL_SUCCESS, user);
        }
    }

    @RequestMapping(value = "/editPassword/", method = RequestMethod.POST)
    @ResponseBody
    public Result editPassword(@RequestBody String password, HttpSession session) {
        User loginUser = (User) session.getAttribute(Constant.USERINFO);
        if (loginUser == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            loginUser.setPassword(password);
            int status = userService.editPassword(loginUser);
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Result logout(HttpSession session) {
        User loginUser = (User) session.getAttribute(Constant.USERINFO);
        if (loginUser == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            session.removeAttribute(Constant.USERINFO);
            return new Result("success", Constant.DEAL_SUCCESS, loginUser);
        }
    }

}
