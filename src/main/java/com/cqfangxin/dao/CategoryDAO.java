package com.cqfangxin.dao;

import com.cqfangxin.domain.Category;

import java.util.List;

public interface CategoryDAO {
    List<Category> getCategoriesByBrandId(Integer brandId);
    int uspert(Category category, String userId);
    int editPicById(Integer cateId, String imageSrc, String userId);
    int deleteCateById(Integer id);
}
