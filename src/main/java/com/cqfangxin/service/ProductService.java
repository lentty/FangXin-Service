package com.cqfangxin.service;

import com.cqfangxin.dao.ProductDAO;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getProductListByCatId(Integer categoryId){
        return productDAO.getProductListByCatId(categoryId);
    }

    public Product getProductById(Integer productId){
        return productDAO.getProductById(productId).get(0);
    }

    public List<ProductImage> getPromotedProductImages(){
        return productDAO.getPromotedProductImages();
    }

    public List<Product> getPromotedProducts(){
        return productDAO.getPromotedProducts();
    }

    public List<Product> searchProductsByName(String keyword){
        return productDAO.searchProductsByName(keyword);
    }

    public List<Product> getProductList(){
        return productDAO.getProductList();
    }
    public void deleteProductById(Integer id){
        productDAO.deleteProductById(id);
    }

    public int saveProduct(Product product){
        return productDAO.saveProduct(product);
    }

}
