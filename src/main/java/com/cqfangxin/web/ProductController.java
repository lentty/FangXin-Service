package com.cqfangxin.web;

import com.cqfangxin.domain.*;
import com.cqfangxin.service.BrandService;
import com.cqfangxin.service.ProductService;
import com.cqfangxin.storage.StorageService;
import com.cqfangxin.utils.Constant;
import com.cqfangxin.utils.TimeFormatUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getProductListByCatId(@PathVariable("id")Integer catId) {
        return productService.getProductListByCatId(catId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result getProductById(@PathVariable("id")Integer productId, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.DEAL_FAIL);
        } else {
            Product product = productService.getProductById(productId);
            return new Result("success", Constant.DEAL_SUCCESS, product);
        }
    }

    @RequestMapping(value = "/promotedImages", method = RequestMethod.GET)
    @ResponseBody
    public List<ProductImage> getPromotedProductImages() {
        return productService.getPromotedProductImages();
    }

    @RequestMapping(value = "/promoted", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getPromotedProducts() {
        return productService.getPromotedProducts();
    }

    @RequestMapping(value = "/search/{key}", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> searchProductsByName(@PathVariable("key")String keyword)
            throws UnsupportedEncodingException{
        List<Product> searchResult =  productService.searchProductsByName(keyword);
        return searchResult;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ResponseBody
    public Result getAllProducts(@RequestBody Pagination<Product> pagination, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.DEAL_FAIL);
        } else {
            List<Product> products = productService.getProductsByPage(pagination);
            TableDataResult resultObj = new TableDataResult();
            resultObj.setTotalCount(pagination.getTotalCount());
            resultObj.setData(products);
            return new Result("success", Constant.DEAL_SUCCESS, resultObj);
        }
    }

    @RequestMapping(value = "/totalCount", method = RequestMethod.POST)
    @ResponseBody
    public Result getTotalProductCount(@RequestBody Pagination<Product> pagination, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.DEAL_FAIL);
        } else {
            int numOfProduct = productService.getTotalProductCount(pagination);
            TableDataResult resultObj = new TableDataResult();
            resultObj.setTotalCount(numOfProduct);
            return new Result("success", Constant.DEAL_SUCCESS, resultObj);
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result upsertProduct(@RequestBody Product product, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.DEAL_FAIL);
        } else {
            int status = productService.upsertProduct(product, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String addNewProduct(Model model){
        model.addAttribute("brandList", brandService.getBrandList());
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result deleteProductById(@PathVariable("id") Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = productService.deleteProductById(id);
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/deletePic/", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteProductPicById(@RequestBody ProductImage productImage, HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            int status = productService.deleteProductPicById(productImage.getFileId());
            if (status > 0) {
                logger.info("Successfully delete product image id " + productImage.getFileId() + " in product_image table");
                int retCode = storageService.deleteFile(productImage.getFileLocation());
                if (retCode == 0) {
                    return new Result("success", status);
                } else {
                    return new Result("fail", retCode);
                }
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute Product product, Model model){
       productService.saveProduct(product);
       return "redirect:/product/admin";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFile(@RequestParam("productId") Integer productId, @RequestParam("file") MultipartFile productPic,
                              HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            String relativePath = "products/"+ TimeFormatUtil.formatDate()+"/";
            String absolutePath = storageService.store(productPic, relativePath);
            logger.info("absolute File Path: " + absolutePath);
            String storePath = relativePath + productPic.getOriginalFilename();
            int status = productService.editPicById(productId, storePath, user.getUsername());
            if (status > 0) {
                return new Result("success", status);
            } else {
                return new Result("fail", status);
            }
        }
    }

    @RequestMapping(value = "/uploadForDetails", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadFileForDetails(@RequestParam("productId") Integer productId,
                                       @RequestParam("fileType") Integer fileType,
                                       @RequestParam("file") MultipartFile productPic,
                             HttpSession session) {
        User user = (User) session.getAttribute(Constant.USERINFO);
        if (user == null) {
            return new Result("fail", Constant.NO_LOGIN_USER);
        } else {
            String relativePath = "products/"+ TimeFormatUtil.formatDate()+"/"+fileType+"/";
            String absolutePath = storageService.store(productPic, relativePath);
            logger.info("absolute File Path: " + absolutePath);
            String storePath = relativePath + productPic.getOriginalFilename();
            ProductImage image =  new ProductImage();
            image.setProductId(productId);
            image.setFileType(fileType);
            image.setFileName(productPic.getOriginalFilename());
            image.setFileLocation(storePath);
            image.setFileSize(productPic.getSize());
            int generatedId = productService.editPicDetailsById(image);
            image.setFileId(generatedId);
            if (generatedId > 0) {
                return new Result("success", "", image);
            } else {
                return new Result("fail", generatedId);
            }
        }
    }
}
