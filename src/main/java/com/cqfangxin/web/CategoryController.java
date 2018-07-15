package com.cqfangxin.web;

import com.cqfangxin.domain.Category;
import com.cqfangxin.domain.Result;
import com.cqfangxin.domain.User;
import com.cqfangxin.service.CategoryService;
import com.cqfangxin.storage.StorageService;
import com.cqfangxin.utils.Constant;
import com.cqfangxin.utils.TimeFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/upsert", method = RequestMethod.POST)
    @ResponseBody
    public Result upsert(@RequestBody Category category, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = categoryService.upsert(category, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result editCatePic(@RequestParam("cateId") Integer cateId, @RequestParam("file") MultipartFile catePic,
                         HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            String relativePath = "categories/"+ TimeFormatUtil.formatDate()+"/";
            String absolutePath = storageService.store(catePic, relativePath);
            logger.info("absolute File Path: " + absolutePath);
            String storePath = relativePath + catePic.getOriginalFilename();
            int status = categoryService.editPicById(cateId, storePath, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteCateById(@PathVariable("id") Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = categoryService.deleteCateById(id);
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }


}
