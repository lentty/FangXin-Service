package com.cqfangxin.service;

import com.cqfangxin.domain.Category;

public interface CategoryService {
    int upsert(Category category, String userId);
    int editPicById(Integer cateId, String imageSrc, String userId);
    int deleteCateById(Integer id);
}
