package com.cqfangxin.service.impl;

import com.cqfangxin.dao.BrandDAO;
import com.cqfangxin.dao.ProductDAO;
import com.cqfangxin.dao.UserDAO;
import com.cqfangxin.domain.HomepageVO;
import com.cqfangxin.domain.Result;
import com.cqfangxin.service.HomepageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Service
public class HomepageServiceImpl implements HomepageService {

    private static final Logger logger = LoggerFactory.getLogger(HomepageServiceImpl.class);

    @Autowired
    private BrandDAO brandDao;

    @Autowired
    private ProductDAO productDao;

    @Autowired
    private UserDAO userDao;

    @Override
    public HomepageVO getHomepageInfo(){
        HomepageVO homepageVO = new HomepageVO();
        homepageVO.setNumOfBrand(brandDao.getNumOfBrand());
        homepageVO.setNumOfUser(userDao.getNumOfUser());
        Map<Integer, Integer> productTypeMap = productDao.getNumOfProduct();
        if(!CollectionUtils.isEmpty(productTypeMap)){
            homepageVO.setNumOfProduct(productTypeMap.get(1));
            homepageVO.setNumOfPromotedProduct(productTypeMap.get(2));
        }
        logger.info(homepageVO.toString());
        return homepageVO;
    }

}
