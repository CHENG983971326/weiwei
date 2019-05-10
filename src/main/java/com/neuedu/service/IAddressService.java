package com.neuedu.service;

import com.neuedu.noUse.ServerResponse;
import com.neuedu.pojo.Shopping;


public interface IAddressService {
    /**
     * 添加收获地址
     */
    public ServerResponse add(Integer userId, Shopping shopping);

    ServerResponse del(Integer userId, Integer shoppingId);

    /**
     * 更新地址
     */
    ServerResponse update(Shopping shopping);

    ServerResponse select(Integer shoppingId);

    ServerResponse list(Integer pageNum, Integer pageSize);
}
