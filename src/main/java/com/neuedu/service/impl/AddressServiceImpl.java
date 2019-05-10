package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.mapper.ShoppingMapper;
import com.neuedu.pojo.Shopping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    ShoppingMapper shoppingMapper;
    @Override
    public ServerResponse add(Integer userId, Shopping shopping) {
         //参数非空校验
        if (shopping==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //添加地址
        shoppingMapper.insert(shopping);
        //返回结果
        Map<String,Integer> map= Maps.newHashMap();
        map.put("shoppingId",shopping.getId());
        return ServerResponse.serverResponseBySuccess(map);
    }

    @Override
    public ServerResponse del(Integer userId, Integer shoppingId) {
       //参数的非空校验
        if (shoppingId==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //删除
        int result=shoppingMapper.deleteByUserIdAndShoppingId(userId, shoppingId);
        //返回结果
       if (result>0){
           return ServerResponse.serverResponseBySuccess();
       }

        return ServerResponse.serverResponseByError("删除失败");
    }

    @Override
    public ServerResponse update(Shopping shopping) {
        //参数非空校验
        if (shopping==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //更新
        int result=shoppingMapper.updateBySelectiveKey(shopping);
        //返回结果
        if (result>0){
            return ServerResponse.serverResponseBySuccess();
        }

        return ServerResponse.serverResponseByError("更新失败");
    }

    @Override
    public ServerResponse select(Integer shoppingId) {
        //参数的非空校验
        if (shoppingId==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //查询
        Shopping shopping=shoppingMapper.selectByPrimaryKey(shoppingId);
        if (shopping==null){
            return ServerResponse.serverResponseByError("没有该收货地址");
        }
        return ServerResponse.serverResponseBySuccess(shopping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shopping> shoppingList=shoppingMapper.selectAll();
        PageInfo pageInfo=new PageInfo(shoppingList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
