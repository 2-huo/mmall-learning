package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.Shop;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ShopListVo;

/**
 * Created by geely
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<String> deleteProduct(Integer productId, String username);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize, String username);
    // modified
    ServerResponse<PageInfo> searchProduct(String productName, String username, int pageNum, int pageSize);

    // modified 0102
    ServerResponse<PageInfo> getShopList(String keyword, int pageNum, int pageSize);

    // modified 0116
    ServerResponse<PageInfo> getShopProductList(int pageNum, int pageSize, Integer shopId);


    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

    // 0120
    ServerResponse<Shop> getShopDetail(String shopname);



}
