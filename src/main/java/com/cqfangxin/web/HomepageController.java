package com.cqfangxin.web;

import com.cqfangxin.domain.HomepageVO;
import com.cqfangxin.domain.Result;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.HomepageService;
import com.cqfangxin.service.ProductService;
import com.cqfangxin.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class HomepageController {
    @Autowired
    private HomepageService homepageService;

    @RequestMapping(value="/homepage", method= RequestMethod.GET)
    @ResponseBody
    public Result index(HttpSession session){
        User admin = (User)session.getAttribute(Constant.USERINFO);
        if(admin != null){
            HomepageVO homepageVO = homepageService.getHomepageInfo();
            return new Result("success", Constant.DEAL_SUCCESS, homepageVO);
        }else{
            return new Result("error", Constant.NO_LOGIN_USER);
        }
    }

}
