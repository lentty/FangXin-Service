package com.cqfangxin.dao;

import com.cqfangxin.domain.Product;
import com.cqfangxin.domain.ShoppingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ShoppingCartDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addToShoppingCart(ShoppingItem item){
        Integer userId = item.getUserId();
        Integer productId = item.getProduct().getId();
        Integer amount = item.getAmount();
        Integer itemCount = jdbcTemplate.queryForObject("select count(*) from shopping_cart where user_id = ? " +
                        "and product_id = ?", new Object[]{userId, productId}, new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                        Integer count = resultSet.getInt(1);
                        return count;
                    }
                });
        if(itemCount > 0){
            return jdbcTemplate.update("update shopping_cart set amount = amount + ? where user_id = ? and product_id = ?",
                    amount, userId, productId);
        }else{
            return jdbcTemplate.update("insert into shopping_cart(user_id, product_id, amount) values (?, ?, ?)",
                    userId, productId, amount);
        }
    }

    public List<ShoppingItem> getShoppingList(Integer userId){
        StringBuilder sql = new StringBuilder();
        sql.append("select p.id, p.name, p.trade_price, p.retail_price,p.image_src,c.id, c.amount "+
                "from shopping_cart c, product p where c.product_id = p.id and c.user_id = ? ");
        return jdbcTemplate.query(sql.toString(), new Object[]{userId}, new RowMapper<ShoppingItem>() {
            @Override
            public ShoppingItem mapRow(ResultSet resultSet, int i) throws SQLException {
                ShoppingItem item = new ShoppingItem();
                item.setUserId(userId);
                Product product = new Product();
                item.setProduct(product);
                product.setId(resultSet.getInt("p.id"));
                product.setName(resultSet.getString("p.name"));
                product.setTradePrice(resultSet.getDouble("p.trade_price"));
                product.setRetailPrice(resultSet.getDouble("p.retail_price"));
                product.setImageSrc(resultSet.getString("p.image_src"));
                item.setProduct(product);
                item.setId(resultSet.getInt("c.id"));
                item.setAmount(resultSet.getInt("c.amount"));
                return item;
            }
        });
    }

    public int deleteItemById(Integer id){
        return jdbcTemplate.update("delete from shopping_cart where id = ?", id);
    }

}
