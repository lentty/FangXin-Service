package com.cqfangxin.dao.impl;

import com.cqfangxin.dao.ProductDAO;
import com.cqfangxin.domain.*;
import com.cqfangxin.utils.TimeFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.*;

@Repository
public class ProductDAOImpl implements ProductDAO {

    Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> getProductsByPage(Pagination pagination){
        int offset = pagination.getStart();
        int limit = pagination.getLength();
        List<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from product ");
        sql.append(generateWhereClause(pagination, objects));
        sql.append("order by last_modified_date desc, id desc limit ?,?");
        objects.add(offset);
        objects.add(limit);
        Object[] params = new Object[objects.size()];
        for(int i = 0; i < objects.size(); i++){
            params[i] = objects.get(i);
        }
        logger.info("get product pagnation sql is : " + sql.toString());
        logger.info("params: "+objects);
        List<Product> result = jdbcTemplate.query(sql.toString(), params, new ProductRowMapper());
        logger.info("result is : "+ result);
        return result;
    }

    private String generateWhereClause(Pagination pagination, List<Object> objects){
        Integer brandId = pagination.getBrandId();
        Integer cateId = pagination.getCateId();
        StringBuilder sql = new StringBuilder();
        if(brandId != null){
            sql.append("where brand_id = ? ");
            objects.add(brandId);
            if(cateId != null){
                sql.append("and cat_id = ? ");
                objects.add(cateId);
            }
        }
        return  sql.toString();
    }

    @Override
    public int getTotalProductCount(Pagination pagination) {

        StringBuilder sql = new StringBuilder();
        List<Object> objects = new ArrayList<>();
        sql.append("select count(1) from product ");
        sql.append(generateWhereClause(pagination, objects));
        logger.info("get product total count sql is : "+sql.toString());
        if (!objects.isEmpty()) {
            Object[] params = new Object[objects.size()];
            for (int i = 0; i < objects.size(); i++) {
                params[i] = objects.get(i);
            }
            logger.info("params: "+objects);
            return jdbcTemplate.queryForObject(sql.toString(), params,
                    new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer totalCount = resultSet.getInt(1);
                            return totalCount;
                        }
                    });
        } else {
            return jdbcTemplate.queryForObject(sql.toString(),
                    new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer totalCount = resultSet.getInt(1);
                            return totalCount;
                        }
                    });
        }
    }

    @Override
    public int upsertProduct(Product product, String username){
        Integer brandId = product.getId();
        if (brandId == null) {
            int existProductCnt = jdbcTemplate.queryForObject("select count(1) from product where code = ? or name = ?",
                    new Object[]{product.getCode(), product.getName()}, new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer numOfBrands = resultSet.getInt(1);
                            return numOfBrands;
                        }
                    });
            if(existProductCnt != 0){
                return -1;
            }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int status = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    StringBuilder sb = new StringBuilder();
                    sb.append("insert into product(cat_id, brand_id, name, spec, status, unit, trade_price," +
                            " retail_price, amount, code, origin, period, create_by, create_date, " +
                            " last_modified_by, last_modified_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                            "  ?, ?, ?, ?)");
                    PreparedStatement ps = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
                    setAttributesForInsert(ps, product, username);
                    return ps;
                }
            }, keyHolder);
            int generatedId = keyHolder.getKey().intValue();
            if(status == 1){
                int[] updateCount = insertToProductDetail(product, generatedId);
                logger.info("insert " + updateCount.length + " records to product_detail ");
            }
            return status;

        } else {
            int status = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    StringBuilder sb = new StringBuilder();
                    sb.append("update product set cat_id = ?, brand_id = ?, name = ?, spec = ?, status = ?," +
                            " unit = ?, trade_price = ?, retail_price = ?, amount = ?, code = ?, origin = ?," +
                            " period = ?, last_modified_by = ?, last_modified_date = ? where id = ? ");
                    PreparedStatement ps = connection.prepareStatement(sb.toString());
                    setAttributesForUpdate(ps, product, username);
                    return ps;
                }
            });
            if(status == 1){
                StringBuilder deleteSql = new StringBuilder();
                deleteSql.append("delete from product_detail where product_id = ?");
                int deleteCount = jdbcTemplate.update(deleteSql.toString(), product.getId());
                logger.info("delete " + deleteCount + " records in product_detail ");
                int[] updateCount = insertToProductDetail(product, product.getId());
                logger.info("insert " + updateCount.length + " records in product_detail ");
            }
            return status;
        }
    }

    private int[] insertToProductDetail(Product product, Integer productId){
        if(!CollectionUtils.isEmpty(product.getDetails())) {
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("insert into product_detail(product_id, attri_key, attri_value) values (?, ?, ?)");
            List<Object[]> batchArgs = new ArrayList<>();
            for (ProductDetail productDetail : product.getDetails()) {
                Object[] args = new Object[]{productId, productDetail.getAttriKey(), productDetail.getAttriValue()};
                batchArgs.add(args);
            }
            int[] updateCount = jdbcTemplate.batchUpdate(insertSql.toString(), batchArgs);
            return updateCount;
        }
        return new int[]{};
    }

    private void setAttributesForInsert(PreparedStatement ps, Product product, String username) throws SQLException{
        int index = setAttributes(ps, product, username);
        ps.setString(index++, username);
        ps.setString(index++, TimeFormatUtil.formatTime());
    }

    private void setAttributesForUpdate(PreparedStatement ps, Product product, String username) throws SQLException{
        int index = setAttributes(ps, product, username);
        ps.setInt(index++, product.getId());
    }

    private int setAttributes(PreparedStatement ps, Product product, String username) throws SQLException{
        int index = 1;
        ps.setInt(index++, product.getCatId());
        ps.setInt(index++, product.getBrandId());
        ps.setString(index++, product.getName());
        ps.setString(index++, product.getSpec());
        ps.setInt(index++, product.getStatus());
        ps.setString(index++, product.getUnit());
        ps.setDouble(index++, product.getTradePrice());
        ps.setDouble(index++, product.getRetailPrice());
        ps.setInt(index++, product.getAmount());
        ps.setString(index++, product.getCode());
        ps.setString(index++, product.getOrigin());
        ps.setString(index++, product.getPeriod());
        ps.setString(index++, username);
        ps.setString(index++, TimeFormatUtil.formatTime());
        return index;
    }

    @Override
    public List<Product> getProductListByBrandId(Integer brandId) {
        return jdbcTemplate.query("select * from product where brand_id = ? and status != 0",
                new Object[]{brandId}, new ProductRowMapper());
    }

    @Override
    public Product getProductById(Integer productId){
        StringBuilder query = new StringBuilder();
        query.append("select p.*, b.brand_name, c.cat_name ")
        .append("from product p, brand b, category c ")
        .append("where p.brand_id = b.brand_id and p.cat_id = c.cat_id and id = ?");
        Product product = jdbcTemplate.queryForObject(query.toString(),
                new Object[]{productId}, new ProductDetailRowMapper());
        if(product == null ){
            return product;
        }
        List<ProductImage> images = jdbcTemplate.query("select * from product_image where product_id = ?",
                new Object[]{productId}, new RowMapper<ProductImage>() {
                    @Override
                    public ProductImage mapRow(ResultSet resultSet, int i) throws SQLException {
                        return populateProductImage(resultSet);
                    }
                });
        product.setImages(images);

        List<ProductDetail> details = jdbcTemplate.query("select * from product_detail where product_id = ?",
                new Object[]{productId}, new RowMapper<ProductDetail>() {
                    @Override
                    public ProductDetail mapRow(ResultSet resultSet, int i) throws SQLException {
                        ProductDetail productDetail = new ProductDetail();
                        productDetail.setProductId(resultSet.getInt("product_id"));
                        productDetail.setAttriKey(resultSet.getString("attri_key"));
                        productDetail.setAttriValue(resultSet.getString("attri_value"));
                        return productDetail;
                    }
                });
        product.setDetails(details);
        return product;
    }

    @Override
    public List<ProductImage> getPromotedProductImages(){
        return  jdbcTemplate.query("select * from product_image", new RowMapper<ProductImage>() {
            @Override
            public ProductImage mapRow(ResultSet resultSet, int i) throws SQLException {
                return  populateProductImage(resultSet);
            }
        });
    }

    private ProductImage populateProductImage(ResultSet resultSet) throws SQLException{
        ProductImage productImage = new ProductImage();
        productImage.setFileId(resultSet.getInt("id"));
        productImage.setProductId(resultSet.getInt("product_id"));
        productImage.setFileType(resultSet.getInt("type"));
        productImage.setFileName(resultSet.getString("name"));
        productImage.setFileLocation(resultSet.getString("location"));
        productImage.setFileSize(resultSet.getLong("size"));
        return productImage;
    }

    @Override
    public List<Product> getPromotedProducts(){
        return jdbcTemplate.query("select * from product where status = 2", new ProductRowMapper());
    }

    @Override
    public List<Product> searchProductsByName(String keyword){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from product where name like concat('%','").append(keyword).append("', '%') and status != 0");
        return jdbcTemplate.query(sb.toString(), new ProductRowMapper());
    }

    @Override
    public List<Product> getProductList(){
        return jdbcTemplate.query("select * from product", new ProductRowMapper());
    }

    @Override
    public int deleteProductById(Integer id){
        int deleteImageCount = jdbcTemplate.update("delete from product_image where id = ?", new Object[]{id});
        logger.info("delete " + deleteImageCount + " records in product_image table");
        int deleteDetailCount = jdbcTemplate.update("delete from product_detail where product_id = ?", new Object[]{id});
        logger.info("delete " + deleteDetailCount + " records in product_detail table");
        int deleteCount = jdbcTemplate.update("delete from product where id = ?", new Object[]{id});
        logger.info("delete product " + id + " in product table");
        return deleteCount;
    }

    @Override
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

    @Override
    public Map<Integer, Integer> getNumOfProduct(){
        Map<Integer, Integer> productTypeCount = new HashMap<>();
        jdbcTemplate.query("select status, count(1) from product group by status",
                new RowMapper<Void>() {
                    @Override
                    public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer productType = resultSet.getInt("status");
                        Integer numOfProduct = resultSet.getInt(2);
                        productTypeCount.put(productType, numOfProduct);
                        return null;
                    }
                });
        return productTypeCount;
    }

    @Override
    public int editPicById(Integer productId, String imageSrc, String userId){
        return jdbcTemplate.update("update product set image_src = ?, last_modified_date = ?, last_modified_by = ?  where id = ?",
                new Object[]{imageSrc, TimeFormatUtil.formatTime(), userId, productId});
    }

    @Override
    public int editPicDetailsById(ProductImage image) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                StringBuilder sb = new StringBuilder();
                sb.append("insert into product_image(product_id, type, name, location, size) values (?, ?, ?, ?, ?)");
                PreparedStatement ps = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                ps.setInt(index++, image.getProductId());
                ps.setInt(index++, image.getFileType());
                ps.setString(index++, image.getFileName());
                ps.setString(index++, image.getFileLocation());
                ps.setLong(index, image.getFileSize());
                return ps;
            }
        }, keyHolder);
        if (status > 0) {
            return keyHolder.getKey().intValue();
        } else {
            return -1;
        }
    }

    @Override
    public int deleteProductPicById(Integer picId){
        return jdbcTemplate.update("delete from product_image where id = ?", new Object[]{picId});
    }

    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = mapProduct(resultSet);
            return  product;
        }
    }

    class ProductDetailRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = mapProduct(resultSet);
            Brand brand = new Brand();
            brand.setId(product.getBrandId());
            brand.setName(resultSet.getString("brand_name"));
            product.setBrand(brand);
            Category category = new Category();
            category.setId(product.getCatId());
            category.setName(resultSet.getString("cat_name"));
            product.setCategory(category);
            return  product;
        }
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException{
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
        product.setCreateBy(resultSet.getString("create_by"));
        product.setCreateDate(TimeFormatUtil.timeFormat(resultSet.getString("create_date")));
        product.setLastModifiedBy(resultSet.getString("last_modified_by"));
        product.setLastModifiedDate(TimeFormatUtil.timeFormat(resultSet.getString("last_modified_date")));
        return product;
    }

}
