package com.cqfangxin.web;

import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import com.cqfangxin.service.BrandService;
import com.cqfangxin.service.ProductService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> getProductListByCatId(@PathVariable("id")Integer catId) {
        return productService.getProductListByCatId(catId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProductById(@PathVariable("id")Integer productId) {
        return productService.getProductById(productId);
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

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getProductList(Model model){
        model.addAttribute("list", productService.getProductList());
        return "product/admin";
    }

    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String editProduct(@RequestParam(name = "id")Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("brandList", brandService.getBrandList());
        model.addAttribute("productInfo", new Product());
        return "product/form";
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String addNewProduct(Model model){
        model.addAttribute("brandList", brandService.getBrandList());
        model.addAttribute("productInfo", new Product());
        return "product/form";
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteProductById(@PathVariable("id") Integer id){
        productService.deleteProductById(id);
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute Product productInfo, Model model){
       productService.saveProduct(productInfo);
       model.addAttribute("list", productService.getProductList());
       return "product/admin";
    }
}
