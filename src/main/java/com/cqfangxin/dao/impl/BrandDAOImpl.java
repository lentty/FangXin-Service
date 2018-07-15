package com.cqfangxin.dao.impl;

import com.cqfangxin.dao.BrandDAO;
import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Category;
import com.cqfangxin.domain.Pagination;
import com.cqfangxin.utils.TimeFormatUtil;
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
public class BrandDAOImpl implements BrandDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Brand> getBrandList(){
        Map<Integer, Brand> brandMap = new HashMap<>();
        jdbcTemplate.query("select * from brand b left outer join category c on b.brand_id = c.brand_id where brand_status = 1",
                new RowMapper<Void>() {
                    @Override
                    public Void mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer brand_id = resultSet.getInt("b.brand_id");
                        Brand brand = brandMap.get(brand_id);
                        if(brand == null){
                            brand = new Brand();
                            brand.setId(brand_id);
                            brand.setName(resultSet.getString("b.brand_name"));
                            brand.setStatus(resultSet.getBoolean("b.brand_status"));
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


    @Override
    public List<Brand> getBrandsByPage(Pagination pagination){
        int offset = pagination.getStart();
        int limit = pagination.getLength();
        pagination.setTotalCount(getTotalBrandCount());
        return jdbcTemplate.query("select * from brand order by last_modified_date desc limit ?,? ", new Object[]{offset, limit},
                new BrandRowMapper());
    }

    @Override
    public int getNumOfBrand() {
        return jdbcTemplate.queryForObject("select count(1) from brand where brand_status = 1",
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer numOfBrands = resultSet.getInt(1);
                        return numOfBrands;
                    }
                });
    }

    @Override
    public int getTotalBrandCount() {
        return jdbcTemplate.queryForObject("select count(1) from brand",
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer totalCount = resultSet.getInt(1);
                        return totalCount;
                    }
                });
    }

    @Override
    public Brand getBrandById(Integer id){
        Brand brand = jdbcTemplate.queryForObject("select * from brand where brand_id = ?", new Object[]{id},
                new BrandRowMapper());
        return brand;
    }

    @Override
    public int upsertBrand(Brand brand, String userId){
        Integer brandId = brand.getId();
        if (brandId == null) {
            int existBrandCnt = jdbcTemplate.queryForObject("select count(1) from brand where brand_name = ?",
                    new Object[]{brand.getName()}, new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer numOfBrands = resultSet.getInt(1);
                            return numOfBrands;
                        }
                    });
             if(existBrandCnt != 0){
                 return -1;
             }
             return jdbcTemplate.update("insert into brand(brand_name, brand_status, create_date, create_by, " +
                            "last_modified_date, last_modified_by) values (?,?,?,?,?,?)",
                    new Object[]{brand.getName(), brand.getStatus(), TimeFormatUtil.formatTime(),
                            userId, TimeFormatUtil.formatTime(), userId});
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("update brand set brand_name = ?, brand_status = ?,last_modified_date = ?, last_modified_by = ?" +
                    " where brand_id = ?");
            return jdbcTemplate.update(sb.toString(),
                    new Object[]{brand.getName(), brand.getStatus(), TimeFormatUtil.formatTime(),
                            userId, brand.getId()});
        }
    }

    @Override
    public int deleteBrandById(Integer id){
        jdbcTemplate.update("delete from category where brand_id = ?", new Object[]{id});
        return jdbcTemplate.update("delete from brand where brand_id = ?", new Object[]{id});
    }

    class BrandRowMapper implements RowMapper<Brand> {
        @Override
        public Brand mapRow(ResultSet resultSet, int i) throws SQLException {
            Brand brand = new Brand();
            brand.setId(resultSet.getInt("brand_id"));
            brand.setName(resultSet.getString("brand_name"));
            brand.setStatus(resultSet.getBoolean("brand_status"));
            brand.setCreateDate(TimeFormatUtil.timeFormat(resultSet.getString("create_date")));
            brand.setCreateBy(resultSet.getString("create_by"));
            brand.setLastModifiedDate(TimeFormatUtil.timeFormat(resultSet.getString("last_modified_date")));
            brand.setLastModifiedBy(resultSet.getString("last_modified_by"));
            return brand;
        }
    }
}
