package com.cqfangxin.web;

import com.cqfangxin.domain.*;
import com.cqfangxin.service.BrandService;
import com.cqfangxin.storage.StorageService;
import com.cqfangxin.utils.Constant;
import com.cqfangxin.utils.TimeFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Brand> getBrandsAndCates() {
        return brandService.getBrandsAndCates();
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Brand> getBrandList() {
        return brandService.getBrandList();
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ResponseBody
    public Result getAllBrands(@RequestBody Pagination<Brand> pagination, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.DEAL_FAIL);
        } else {
            List<Brand> brands = brandService.getBrandsByPage(pagination);
            TableDataResult resultObj = new TableDataResult();
            resultObj.setTotalCount(pagination.getTotalCount());
            resultObj.setData(brands);
            return new Result("success", Constant.DEAL_SUCCESS, resultObj);
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Result getBrandById(@PathVariable(name = "id")Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constant.USERINFO);
        if(user == null){
            return new Result("fail", Constant.NO_LOGIN_USER);
        }else {
            Brand brand = brandService.getBrandById(id);
            return new Result("success", Constant.DEAL_SUCCESS, brand);
        }
    }

    @RequestMapping(value = "/upsert", method = RequestMethod.POST)
    @ResponseBody
    public Result upsert(@RequestBody Brand brand, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = brandService.upsertBrand(brand, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteBrandById(@PathVariable("id") Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = brandService.deleteBrandById(id);
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result editBrandPic(@RequestParam("brandId") Integer brandId, @RequestParam("file") MultipartFile brandPic,
                              HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            String relativePath = "brandLogos/"+ TimeFormatUtil.formatDate()+"/";
            String absolutePath = storageService.store(brandPic, relativePath);
            logger.info("absolute File Path: " + absolutePath);
            String storePath = relativePath + brandPic.getOriginalFilename();
            int status = brandService.editPicById(brandId, storePath, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }
}
