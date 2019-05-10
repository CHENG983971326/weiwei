package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.mapper.CartMapper;
import com.neuedu.mapper.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //参数的非空校验
          if (productId==null||count==null){
            return ServerResponse.serverResponseByError("参数不能为空");
               }
             Product product= productMapper.selectByPrimaryKey(productId);
          if (product==null){
              return ServerResponse.serverResponseByError("添加商品不存在");
          }
        //根据productId和userId查询
              Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart==null) {
            //添加
            Cart cart1=new Cart(1,2,3,4,new BigDecimal(0));
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1=new Cart(1,2,3,4,new BigDecimal(0));
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO=getCartVOLimit(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO=new CartVO();
        //根据userId查询购物信息--List<Cart>
        List<Cart> cartList=cartMapper.selectCartByUserId(userId);
        //List<Cart>-->List<CartProductVI>
        List<CartProductVO> cartProductVOList= Lists.newArrayList();
        BigDecimal carttotalprice=new BigDecimal("0");
        if (cartList!=null&&cartList.size()>0){
        for (Cart cart:cartList){
        CartProductVO cartProductVO=new CartProductVO();
        cartProductVO.setId(cart.getId());
        cartProductVO.setQuantity(cart.getQuantity());
        cartProductVO.setUserId(userId);
        cartProductVO.setProductChecked(cart.getChecked());
       //查询商品
        Product product=productMapper.selectByPrimaryKey(cart.getProductId());
        if (product!=null){
          cartProductVO.setProductId(cart.getProductId());
          cartProductVO.setProductMainImage(product.getMainImage());
          cartProductVO.setProductName(product.getName());
          cartProductVO.setProductPrice(product.getPrice());
          cartProductVO.setProductStatus(product.getStatus());
          cartProductVO.setProductStock(product.getStock());
           cartProductVO.setProductSubtitle(product.getSubtitle());
          int stock=product.getStock();
          int limitProductCount=0;
          if (stock>=cart.getQuantity()){
              limitProductCount=cart.getQuantity();
              cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
          }else {
              //库存不足
              limitProductCount=stock;
              //更新购物车中商品的数量
              Cart cart1=new Cart(1,2,3,4,new BigDecimal(0));
              cart1.setId(cart.getId());
              cart1.setQuantity(stock);
              cart1.setProductId(cart.getProductId());
              cart1.setChecked(cart.getChecked());
              cart1.setUserId(userId);
              cartMapper.updateByPrimaryKey(cart1);
              cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
          }

          cartProductVO.setQuantity(limitProductCount);
          cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));

      }
      if (cartProductVO.getProductChecked()==Const.CartCheckedEnum.PRODUCT_CHECKED.getCode()){
          carttotalprice=BigDecimalUtils.add(carttotalprice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
      }

      cartProductVOList.add(cartProductVO);
    }
}
        //计算总价格
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCarttotalprice(carttotalprice);
       //判断购物车是否全选
        int count=cartMapper.isCheckedAll(userId);
        if (count>0){
        cartVO.setIsallchecked(false);
        }else {
          cartVO.setIsallchecked(true);
      }
           return cartVO;
    }

    /**
     *
     */
    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO=getCartVOLimit(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
          //参数的非空校验
        if (productId==null||count==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //查询购物车中的商品
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if (cart!=null){
            //更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
       //返回catvo

        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
       //参数的非空校验
        if (productIds==null||productIds.equals("")){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //将productids--》list<Integer>
        List<Integer>productIdList=Lists.newArrayList();
        String[] productIdsArr=productIds.split(",");
       if (productIdsArr!=null&&productIdsArr.length>0){
           for (String productIdstr:productIdsArr){
               Integer productId=Integer.parseInt(productIdstr);
               productIdList.add(productId);
           }
       }
        //调用dao
  cartMapper.deleteByUserIdAndProductIds(userId,productIdList);
        //返回结果

        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        //参数的非空校验
        /*if (productId==null||productId.equals("")){
            return ServerResponse.serverResponseByError("参数不能为空");
        }*/
        //dao接口
      cartMapper.selectOrUnselectProduct(userId,productId,check);
        //返回结果
        return ServerResponse.serverResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
     int quantity=cartMapper.get_cart_product_count(userId);

        return ServerResponse.serverResponseBySuccess(quantity);
    }
}
