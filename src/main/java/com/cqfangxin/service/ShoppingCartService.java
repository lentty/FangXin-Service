package com.cqfangxin.service;

import com.cqfangxin.dao.ShoppingCartDAO;
import com.cqfangxin.domain.ShoppingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartDAO cartDao;

    public int addToShoppingCart(ShoppingItem item){
        return cartDao.addToShoppingCart(item);
    }

    public List<ShoppingItem> getShoppingList(Integer userId){
        return cartDao.getShoppingList(userId);
    }

    public int deleteItemById(Integer id){
        return cartDao.deleteItemById(id);
    }
}
