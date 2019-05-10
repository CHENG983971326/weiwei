package com.neuedu.controller.proscenium;

import com.neuedu.common.Const;
import com.neuedu.common.ResultModel;
import com.neuedu.mapper.CartMapper;
import com.neuedu.mapper.OrderMapper;
import com.neuedu.mapper.ProductMapper;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import com.neuedu.utils.OrderNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    ICartService cartService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;

    /**
     * 购物车列表
     */
    @RequestMapping(value = "/cart/list.do")
    public String toList(){
        return "/customer/cartList";
    }

    @GetMapping(value = "/cart/list")
    @ResponseBody
    public List<Cart> list(HttpSession session){
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        List<Cart> cartList=cartMapper.selectCartByUserId(userInfo.getId());
        return cartList;
    }
    /**
     * 取消选定购物车中某项商品
     */
    @RequestMapping ("/cart/cancel")
    @ResponseBody()
    public ResultModel fail(String  id) {
        System.out.println(id);
       Cart cart= cartMapper.selectByPrimaryKey(Integer.valueOf(id));
        System.out.println(cart.toString());
       cart.setChecked(0);
        System.out.println(cart.getTotalPrice());
      int result= cartMapper.updateByPrimaryKey(cart);
      if (result>0){
          return ResultModel.build(200,"取消选定成功");
      }
      else {
          return ResultModel.build(0,"取消选定失败");
      }

    }
    /**
     * 选定购物车中某项商品
     */
    @RequestMapping ("/cart/pass")
    @ResponseBody()
    public ResultModel pass(String  id){
        System.out.println(id);
        Cart cart= cartMapper.selectByPrimaryKey(Integer.valueOf(id));
        cart.setChecked(1);
        int result= cartMapper.updateByPrimaryKey(cart);
        if (result>0){
            return ResultModel.build(200,"取消选定成功");
        }
        else {
            return ResultModel.build(0,"取消选定失败");
        }

    }
    /**
     * 查看商品详情
     */
    @GetMapping("/cart/cartInfo/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
       Cart cart=cartMapper.selectByPrimaryKey(Integer.valueOf(id));
       Product product=productMapper.selectByPrimaryKey(cart.getProductId());
        String imgUrl="/file/"+product.getMainImage().substring(8);
        product.setMainImage(imgUrl);
       model.addAttribute("cart", cart);
       model.addAttribute("product",product);
       return  "/customer/cartinfo";
    }
    /**
     * 删除购物车中某项商品
     */
    @RequestMapping ("/cart/delete")
    @ResponseBody()
    public ResultModel delete(String  id,HttpSession session){
        Cart cart=cartMapper.selectByPrimaryKey(Integer.valueOf(id));
        int result= cartMapper.deleteByPrimaryKey(Integer.valueOf(id));
        if (result>0){
           Product product=productMapper.selectByPrimaryKey(cart.getProductId());
           product.setStock(product.getStock()+cart.getQuantity());
           productMapper.updateByPrimaryKey(product);
            return ResultModel.build(200,"删除商品成功");
        }
        else {
            return ResultModel.build(0,"删除商品失败");
        }

    }




}
