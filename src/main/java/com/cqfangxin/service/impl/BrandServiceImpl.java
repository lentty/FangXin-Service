package com.cqfangxin.service.impl;

import com.cqfangxin.dao.BrandDAO;
import com.cqfangxin.dao.CategoryDAO;
import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Pagination;
import com.cqfangxin.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDAO brandDao;

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public List<Brand> getBrandList(){
        return brandDao.getBrandList();
    }

    @Override
    public List<Brand> getBrandsAndCates(){
        return brandDao.getBrandsAndCates();
    }

    @Override
    public List<Brand> getBrandsByPage(Pagination pagination){
        return brandDao.getBrandsByPage(pagination);
    }

    @Override
    public Brand getBrandById(Integer id){
        Brand brand =  brandDao.getBrandById(id);
        brand.setCateList(categoryDAO.getCategoriesByBrandId(id));
        return brand;
    }

    @Override
    public int upsertBrand(Brand brand, String userId){
        return brandDao.upsertBrand(brand, userId);
    }

    @Override
    public int deleteBrandById(Integer id){
        return brandDao.deleteBrandById(id);
    }

    @Override
    public int editPicById(Integer brandId, String imageSrc, String userId){
        return brandDao.editPicById(brandId, imageSrc, userId);
    }
}
