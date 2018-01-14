package com.cqfangxin.web;

import com.cqfangxin.domain.LoginBean;
import com.cqfangxin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private ProductService productService;

    @GetMapping("/login")
    public String login(Model model) throws IOException {
        model.addAttribute("loginInfo", new LoginBean());
        return "login";
    }

    @RequestMapping(value="/doLogin", method= RequestMethod.POST)
    public String doLogin(@ModelAttribute LoginBean loginInfo, Model model){
        model.addAttribute("list", productService.getProductList());
        return "index";
    }

}
