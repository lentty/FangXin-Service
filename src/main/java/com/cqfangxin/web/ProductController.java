package com.cqfangxin.web;

import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import com.cqfangxin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public List<Product> getProductListByCatId(@PathVariable("id")Integer catId) {
        return productService.getProductListByCatId(catId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable("id")Integer productId) {
        return productService.getProductById(productId);
    }

    @RequestMapping(value = "/promotedImages", method = RequestMethod.GET)
    public List<ProductImage> getPromotedProductImages() {
        return productService.getPromotedProductImages();
    }

    @RequestMapping(value = "/promoted", method = RequestMethod.GET)
    public List<Product> getPromotedProducts() {
        return productService.getPromotedProducts();
    }

    @RequestMapping(value = "/search/{key}", method = RequestMethod.GET)
    public List<Product> searchProductsByName(@PathVariable("key")String keyword)
            throws UnsupportedEncodingException{
        List<Product> searchResult =  productService.searchProductsByName(keyword);
        return searchResult;
    }



}
