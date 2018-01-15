package com.cqfangxin.dao;

import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BrandDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Brand> getBrandList(){
        Map<Integer, Brand> brandMap = new HashMap<>();
        jdbcTemplate.query("select * from brand b left outer join category c on b.brand_id = c.brand_id",
                new RowMapper<Void>() {
            @Override
            public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                Integer brand_id = resultSet.getInt("b.brand_id");
                Brand brand = brandMap.get(brand_id);
                if(brand == null){
                    brand = new Brand();
                    brand.setId(brand_id);
                    brand.setName(resultSet.getString("b.brand_name"));
                    brand.setStatus(resultSet.getInt("b.brand_status"));
                    brandMap.put(brand_id, brand);
                }
                List<Category> cateList = brand.getCateList();
                if(cateList == null){
                    cateList = new ArrayList<>();
                    brand.setCateList(cateList);
                }
                Category cat = new Category();
                Integer cate_id = resultSet.getInt("c.cat_id");
                if(cate_id != 0){
                    cat.setId(cate_id);
                    cat.setBrandId(resultSet.getInt("c.brand_id"));
                    cat.setName(resultSet.getString("c.cat_name"));
                    cat.setImageSrc(resultSet.getString("c.image_src"));
                    cateList.add(cat);
                }
                return null;
            }
        });
        return new ArrayList<>(brandMap.values());
    }

    public List<Category> getCategoriesByBrandId(Integer brandId) {
        return jdbcTemplate.query("select * from category where brand_id = ?",
                new Object[]{brandId}, new RowMapper<Category>() {
                    @Override
                    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
                        Category cat = new Category();
                        cat.setId(resultSet.getInt("cat_id"));
                        cat.setBrandId(resultSet.getInt("brand_id"));
                        cat.setName(resultSet.getString("cat_name"));
                        return cat;
                    }
                });
    }

    public List<Brand> getAllBrands(){
        return jdbcTemplate.query("select * from brand ", new BrandRowMapper());
    }

    class BrandRowMapper implements RowMapper<Brand> {
        @Override
        public Brand mapRow(ResultSet resultSet, int i) throws SQLException {
            Brand brand = new Brand();
            brand.setId(resultSet.getInt("brand_id"));
            brand.setName(resultSet.getString("brand_name"));
            brand.setStatus(resultSet.getInt("brand_status"));
            return brand;
        }
    }
}
