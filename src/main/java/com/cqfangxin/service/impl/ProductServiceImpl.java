package com.cqfangxin.service.impl;

import com.cqfangxin.dao.ProductDAO;
import com.cqfangxin.domain.Pagination;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import com.cqfangxin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    public List<Product> getProductListByBrandId(Integer brandId){
        return productDAO.getProductListByBrandId(brandId);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int editPicById(ProductImage image,  String userId){
        int generatedId = editPicDetailsById(image);
        image.setFileId(generatedId);
        return productDAO.editPicById(image.getProductId(), image.getFileLocation(), userId);
    }

    @Override
    public int editPicDetailsById(ProductImage image){
        return productDAO.editPicDetailsById(image);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int deleteProductPicById(ProductImage image, String userId){
        if(image.getFileType() == 1){
            productDAO.editPicById(image.getProductId(), null, userId);
        }
        return productDAO.deleteProductPicById(image.getFileId());
    }

}
