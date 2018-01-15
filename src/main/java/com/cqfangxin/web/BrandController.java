package com.cqfangxin.web;

import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Category;
import com.cqfangxin.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Brand> getBrandList() {
        return brandService.getBrandList();
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAllBrands(Model model){
        model.addAttribute("list", brandService.getAllBrands());
        return "brand/admin";
    }
}
