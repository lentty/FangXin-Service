package com.cqfangxin.service;

import com.cqfangxin.domain.Brand;
import com.cqfangxin.domain.Pagination;

import java.util.List;


public interface BrandService {

    List<Brand> getBrandList();

    List<Brand> getBrandsAndCates();

    List<Brand> getBrandsByPage(Pagination pagination);

    Brand getBrandById(Integer id);

    int upsertBrand(Brand brand, String userId);

    int deleteBrandById(Integer id);

    int editPicById(Integer brandId, String imageSrc, String userId);
}
