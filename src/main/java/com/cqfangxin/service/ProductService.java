package com.cqfangxin.service;

import com.cqfangxin.domain.Pagination;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;

import java.util.List;


public interface ProductService {

     List<Product> getProductsByPage(Pagination pagination);

     int getTotalProductCount(Pagination pagination);

     int upsertProduct(Product product, String username);

     List<Product> getProductListByCatId(Integer categoryId);

     Product getProductById(Integer productId);

     List<ProductImage> getPromotedProductImages();

     List<Product> getPromotedProducts();

     List<Product> searchProductsByName(String keyword);

     List<Product> getProductList();

     int deleteProductById(Integer id);

     int saveProduct(Product product);

     int editPicById(Integer productId, String imageSrc, String userId);

     int editPicDetailsById(ProductImage image);

     int deleteProductPicById(Integer picId);

}
