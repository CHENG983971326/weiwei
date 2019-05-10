package com.neuedu.service;

import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductInsert;
import com.neuedu.common.ResultModel;


public interface IProductService {

    /**
     * 添加商品
     * */
    ResultModel insertProduct(ProductInsert product);
    /**
     * 商品上下架
     * */
    ResultModel set_on_status(Product product);
    ResultModel set_out_status(Product product);
//    /**
//     * 查询商品详情
//     */
//    ServerResponse detail(Integer productId);
//    /**
//     * 后台-商品列表，分页
//     */
//    String list(Integer pageNum, Integer pageSize);
//    /**
//     * 后台查询商品
//     */
//    ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize);
//    /**
//     * 图片上传
//     */
//    ServerResponse upload(MultipartFile file, String path);
//    /**
//     * 前台-查看商品详情
//     */
//    ServerResponse detail_portal(Integer productId);
//    /**
//     * 前台-商品搜索及排序（模糊查询）
//     */
//    ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
