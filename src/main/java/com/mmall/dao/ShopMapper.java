package com.mmall.dao;

import com.mmall.pojo.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
public interface ShopMapper {
    Shop selectByPrimaryKey(Integer id);

    int insert(Shop record);

    int insertSelective(Shop record);

    int updateByPrimaryKeySelective(Shop record);

    List<Shop> selectByName(String shopname);
}
