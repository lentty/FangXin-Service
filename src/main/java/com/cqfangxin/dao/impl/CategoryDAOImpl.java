package com.cqfangxin.dao.impl;

import com.cqfangxin.dao.CategoryDAO;
import com.cqfangxin.domain.Category;
import com.cqfangxin.utils.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getCategoriesByBrandId(Integer brandId) {
        return jdbcTemplate.query("select * from category where brand_id = ? order by last_modified_date desc",
                new Object[]{brandId}, new RowMapper<Category>() {
                    @Override
                    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
                        Category cat = new Category();
                        cat.setId(resultSet.getInt("cat_id"));
                        cat.setBrandId(resultSet.getInt("brand_id"));
                        cat.setName(resultSet.getString("cat_name"));
                        cat.setImageSrc(resultSet.getString("image_src"));
                        cat.setLastModifiedDate(TimeFormatUtil.timeFormat(resultSet.getString("last_modified_date")));
                        cat.setLastModifiedBy(resultSet.getString("last_modified_by"));
                        return cat;
                    }
                });
    }

    @Override
    public int uspert(Category category, String userId) {
        Integer cateId = category.getId();
        if (cateId == null) {
            int existCateCnt = jdbcTemplate.queryForObject("select count(1) from category where brand_id = ? and cat_name = ?",
                    new Object[]{category.getBrandId(), category.getName()}, new RowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                            Integer numOfBrands = resultSet.getInt(1);
                            return numOfBrands;
                        }
                    });
            if (existCateCnt != 0) {
                return -1;
            }
            StringBuilder sb = new StringBuilder("insert into category(brand_id, cat_name, image_src, create_date," +
                    "create_by, last_modified_date, last_modified_by) values (?,?,?,?,?,?,?)");
            return jdbcTemplate.update(sb.toString(), new Object[]{category.getBrandId(), category.getName(),
                    category.getImageSrc(), TimeFormatUtil.formatTime(), userId, TimeFormatUtil.formatTime(), userId});
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("update category set cat_name = ?, image_src = ?, last_modified_date = ?, last_modified_by = ? " +
                    " where brand_id = ? and cat_id = ?");
            return jdbcTemplate.update(sb.toString(), new Object[]{category.getName(), category.getImageSrc(),
                     TimeFormatUtil.formatTime(), userId, category.getBrandId(), category.getId()});
        }
    }

    @Override
    public int editPicById(Integer cateId, String imageSrc, String userId){
        return jdbcTemplate.update("update category set image_src = ?, last_modified_date = ?, last_modified_by = ?  where cat_id = ?",
                new Object[]{imageSrc, TimeFormatUtil.formatTime(), userId, cateId});
    }

    @Override
    public int deleteCateById(Integer id){
        return jdbcTemplate.update("delete from category where cat_id = ?", new Object[]{id});
    }
}
