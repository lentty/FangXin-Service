package com.cqfangxin.web;

import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ShoppingItem;
import com.cqfangxin.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public int addToShopingCart(@RequestParam(value = "userId") Integer userId,
                                @RequestParam(value = "productId") Integer productId,
                                @RequestParam(value = "amount") Integer amount){
        ShoppingItem item = new ShoppingItem();
        item.setUserId(userId);
        item.setProduct(new Product(productId));
        item.setAmount(amount);
        return cartService.addToShoppingCart(item);
    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public List<ShoppingItem> getShoppingList(@PathVariable("id")Integer userId) {
        return cartService.getShoppingList(userId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public int deleteItemById(@PathVariable("id") Integer id) {
        return cartService.deleteItemById(id);
    }

}
