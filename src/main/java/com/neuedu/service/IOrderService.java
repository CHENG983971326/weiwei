package com.neuedu.service;

import com.neuedu.noUse.ServerResponse;

public interface IOrderService {
    /**
     * 创建订单
     */
    ServerResponse createOrder(Integer userId, Integer shoppingId);
    /**
     * 取消订单
     */
    ServerResponse cancel(Integer userId, Long orderNo);
    /**
     * 获取购物车中订单明细
     */
    ServerResponse get_order_cart_product(Integer userId);
    /**
     * 订单列表
     */
    ServerResponse list(Integer userId, Integer pageNum, Integer pageSize);
    /**
     * 订单详情
     */
    ServerResponse detail(Long orderNo);
    /**
     * 发货
     */
    ServerResponse send_goods(Integer userId, Long orderNo);
}
