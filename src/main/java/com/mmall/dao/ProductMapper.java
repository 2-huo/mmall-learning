package com.mmall.dao;

import com.mmall.pojo.Product;
import com.mmall.pojo.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("username") String username);

    int deleteByAdmin(Integer id);

    int deleteByShopname(String shopname);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList(String username);

    // 0404
    List<Product> selectAllProduct();

//    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);
    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("username") String username);

    List<Product> selectByNameAndCategoryIds(String productName);


}