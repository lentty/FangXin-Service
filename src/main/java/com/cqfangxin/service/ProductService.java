package com.cqfangxin.service;

import com.cqfangxin.domain.Pagination;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;

import java.util.List;


public interface ProductService {

     List<Product> getProductsByPage(Pagination pagination);

     int getTotalProductCount(Pagination pagination);

     int upsertProduct(Product product, String username);

     List<Product> getProductListByBrandId(Integer brandId);

     Product getProductById(Integer productId);

     List<ProductImage> getPromotedProductImages();

     List<Product> getPromotedProducts();

     List<Product> searchProductsByName(String keyword);

     List<Product> getProductList();

     int deleteProductById(Integer id);

     int saveProduct(Product product);

     int editPicById(ProductImage image, String userId);

     int editPicDetailsById(ProductImage image);

     int deleteProductPicById(ProductImage productImage, String userId);

}
