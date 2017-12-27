package com.cqfangxin.service;

import com.cqfangxin.dao.BrandDAO;
import com.cqfangxin.domain.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandDAO brandDao;

    public List<Brand> getBrandList(){
        return brandDao.getBrandList();
    }
}
