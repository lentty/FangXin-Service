package com.cqfangxin.service.impl;

import com.cqfangxin.dao.CategoryDAO;
import com.cqfangxin.domain.Category;
import com.cqfangxin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public int upsert(Category category, String userId){
        return categoryDAO.uspert(category, userId);
    }

    @Override
    public int editPicById(Integer cateId, String imageSrc, String userId){
        return categoryDAO.editPicById(cateId, imageSrc, userId);
    }

    @Override
    public int deleteCateById(Integer id){
        return categoryDAO.deleteCateById(id);
    }

}
