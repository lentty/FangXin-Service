package com.cqfangxin.web;

import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Category;
import com.cqfangxin.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Brand> getBrandList() {
        return brandService.getBrandList();
    }


}
