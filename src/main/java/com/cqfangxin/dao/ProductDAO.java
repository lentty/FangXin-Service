package com.cqfangxin.dao;

import com.cqfangxin.domain.FileMeta;
import com.cqfangxin.domain.Pagination;
import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;

import java.util.List;
import java.util.Map;

public interface ProductDAO {

     List<Product> getProductsByPage(Pagination pagination);

     int getTotalProductCount();

     int upsertProduct(Product product, String username);

     List<Product> getProductListByCatId(Integer categoryId) ;

     Product getProductById(Integer productId);

     List<ProductImage> getPromotedProductImages();

     List<Product> getPromotedProducts();

     List<Product> searchProductsByName(String keyword);

     List<Product> getProductList();

     int deleteProductById(Integer id);

     int saveProduct(Product product);

     Map<Integer, Integer> getNumOfProduct();

     int editPicById(Integer productId, String imageSrc, String userId);

     int editPicDetailsById(ProductImage image);

     int deleteProductPicById(Integer picId);
}
