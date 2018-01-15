package com.cqfangxin.dao;

import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> getProductListByCatId(Integer categoryId) {
        return jdbcTemplate.query("select * from product where cat_id = ? and status != 0",
                new Object[]{categoryId}, new ProductRowMapper());
    }

    public List<Product> getProductById(Integer productId){
        List<Product> productList = jdbcTemplate.query("select * from product where id =  ? and status != 0",
                new Object[]{productId}, new ProductRowMapper());
        if(productList == null || productList.isEmpty()){
            return Collections.emptyList();
        }
        Product product = productList.get(0);
        List<ProductImage> images = jdbcTemplate.query("select * from product_image where product_id = ? and status != 0 ",
                new Object[]{productId}, new RowMapper<ProductImage>() {
                    @Override
                    public ProductImage mapRow(ResultSet resultSet, int i) throws SQLException {
                        ProductImage productImage = new ProductImage();
                        productImage.setId(resultSet.getInt("id"));
                        productImage.setSrc(resultSet.getString("image_src"));
                        productImage.setProductId(resultSet.getInt("product_id"));
                        return productImage;
                    }
                });
        product.setImages(images);
        return productList;
    }

    public List<ProductImage> getPromotedProductImages(){
        return  jdbcTemplate.query("select * from product_image where status = 2", new RowMapper<ProductImage>() {
            @Override
            public ProductImage mapRow(ResultSet resultSet, int i) throws SQLException {
                ProductImage productImage = new ProductImage();
                productImage.setId(resultSet.getInt("id"));
                productImage.setProductId(resultSet.getInt("product_id"));
                productImage.setSrc(resultSet.getString("image_src"));
                productImage.setStatus(resultSet.getInt("status"));
                return  productImage;
            }
        });
    }

    public List<Product> getPromotedProducts(){
        return jdbcTemplate.query("select * from product where status = 2", new ProductRowMapper());
    }

    public List<Product> searchProductsByName(String keyword){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from product where name like concat('%','").append(keyword).append("', '%') and status != 0");
        return jdbcTemplate.query(sb.toString(), new ProductRowMapper());
    }

    public List<Product> getProductList(){
        return jdbcTemplate.query("select * from product", new ProductRowMapper());
    }

    public void deleteProductById(Integer id){
        jdbcTemplate.update("delete from product where id = ?", new Object[]{id});
    }

    public int saveProduct(Product product){
        Integer id = product.getId();
        if(id == null){
            return jdbcTemplate.update("insert into product(cat_id, brand_id, name, spec, unit, trade_price, " +
                            "retail_price, amount, code, origin, period) values (?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{product.getCatId(), product.getBrandId(), product.getName(), product.getSpec(),
                    product.getUnit(), product.getTradePrice(), product.getRetailPrice(), product.getAmount(),
                    product.getCode(), product.getOrigin(), product.getPeriod()});
        } else {
            StringBuilder sql = new StringBuilder();
            sql.append("update product set cat_id = ?, brand_id = ?, name = ?, spec = ?, unit = ?, trade_price = ?,")
               .append("retail_price = ?, amount = ?, code = ?, origin = ?, period = ?  where id = ?");
            return jdbcTemplate.update(sql.toString(), new Object[]{product.getCatId(), product.getBrandId(), product.getName(), product.getSpec(),
                    product.getUnit(), product.getTradePrice(), product.getRetailPrice(), product.getAmount(),
                    product.getCode(), product.getOrigin(), product.getPeriod(), product.getId()});
        }
    }

    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setCatId(resultSet.getInt("cat_id"));
            product.setBrandId(resultSet.getInt("brand_id"));
            product.setName(resultSet.getString("name"));
            product.setSpec(resultSet.getString("spec"));
            product.setStatus(resultSet.getInt("status"));
            product.setUnit(resultSet.getString("unit"));
            product.setTradePrice(resultSet.getDouble("trade_price"));
            product.setRetailPrice(resultSet.getDouble("retail_price"));
            product.setAmount(resultSet.getInt("amount"));
            product.setImageSrc(resultSet.getString("image_src"));
            product.setCode(resultSet.getString("code"));
            product.setOrigin(resultSet.getString("origin"));
            product.setPeriod(resultSet.getString("period"));
            return product;
        }
    }



}
