package com.cqfangxin.service.impl;

import com.cqfangxin.dao.ProductDAO;
import com.cqfangxin.domain.Pagination;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import com.cqfangxin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;

    @Override
    public List<Product> getProductsByPage(Pagination pagination){
        return productDAO.getProductsByPage(pagination);
    }

    @Override
    public int getTotalProductCount(Pagination pagination){
        return productDAO.getTotalProductCount(pagination);
    }

    @Override
    public int upsertProduct(Product product, String username){
        return productDAO.upsertProduct(product, username);
    }

    @Override
    public List<Product> getProductListByCatId(Integer categoryId){
        return productDAO.getProductListByCatId(categoryId);
    }

    @Override
    public Product getProductById(Integer productId){
        return productDAO.getProductById(productId);
    }

    @Override
    public List<ProductImage> getPromotedProductImages(){
        return productDAO.getPromotedProductImages();
    }

    @Override
    public List<Product> getPromotedProducts(){
        return productDAO.getPromotedProducts();
    }

    @Override
    public List<Product> searchProductsByName(String keyword){
        return productDAO.searchProductsByName(keyword);
    }

    @Override
    public List<Product> getProductList(){
        return productDAO.getProductList();
    }

    @Override
    public int deleteProductById(Integer id){
        return productDAO.deleteProductById(id);
    }

    @Override
    public int saveProduct(Product product){
        return productDAO.saveProduct(product);
    }

    @Override
    public int editPicById(Integer productId, String imageSrc, String userId){
        return productDAO.editPicById(productId, imageSrc, userId);
    }

    @Override
    public int editPicDetailsById(ProductImage image){
        return productDAO.editPicDetailsById(image);
    }

    @Override
    public int deleteProductPicById(Integer picId){
        return productDAO.deleteProductPicById(picId);
    }

}
