package com.neuedu.service;

import com.neuedu.noUse.ServerResponse;

public interface ICategoryService {
    /**
     * 获取子类别
   */
    ServerResponse get_category(Integer categoryId);
    /**
     * 增加节点
     */
    ServerResponse add_category(Integer parentId, String categoryName);
/**
 * 修改节点
 */
ServerResponse set_category_name(Integer categoryId, String categoryName);
/**
 * 获取当前id及递归子节点的categoryid
 */
ServerResponse get_deep_category(Integer categoryId);
}
