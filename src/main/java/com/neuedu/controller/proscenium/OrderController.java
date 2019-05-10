package com.neuedu.controller.proscenium;

import com.neuedu.common.Const;
import com.neuedu.common.ResultModel;
import com.neuedu.mapper.*;
import com.neuedu.noUse.ServerResponse;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.OrderNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    IOrderService orderService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ShoppingMapper shoppingMapper;
    @Autowired
    ProductMapper productMapper;

    /**
     * 创建订单
     */
    @GetMapping("/order/commit")
    @ResponseBody()
    public ResultModel commit(HttpSession session) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        List<Cart> list = cartMapper.findCartListByUseIdAndChecked(userInfo.getId());
        session.setAttribute("lists",list);
        BigDecimal bigDecimal = cartMapper.findPayMentByUseIdAndChecked(userInfo.getId());
        int result = cartMapper.batchDelete(list);
        list.clear();
        if (result > 0) {
            Order order = new Order(userInfo.getId(), Long.valueOf(OrderNo.toOrderNo()), bigDecimal, Const.PaymentEnum.ONLINE.getCode(), 10, Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
            orderMapper.insert(order);
            return ResultModel.build(200, "提交成功");
        } else {
            return ResultModel.build(0, "提交失败");
        }

    }

    /**
     * 查看订单
     */
    @RequestMapping("/order/list.do")
    public String toOrder() {
        return "/customer/orderList";
    }

    @GetMapping(value = "/order/list")
    @ResponseBody
    public List<Order> orderList(HttpSession session) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        return orderMapper.findOrderByUserId(userInfo.getId());
    }

    /**
     * 查看订单详情
     */
    @GetMapping("/order/orderInfo/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        Order order = orderMapper.selectByPrimaryKey(Integer.valueOf(id));
        model.addAttribute("order", order);
        return "/customer/orderInfo";
    }

    /**
     * 取消订单
     */
    @GetMapping("/order/delete")
    @ResponseBody()
    public ResultModel delete(String id,HttpSession session) {
        int result = orderMapper.deleteByPrimaryKey(Integer.valueOf(id));
        if (result > 0) {
           List<Cart> cartList= (List<Cart>)session.getAttribute("lists");
            System.out.println(cartList.toString());
           for (Cart cart:cartList){
               Product product=productMapper.selectByPrimaryKey(cart.getProductId());
               product.setStock(product.getStock()+cart.getQuantity());
               productMapper.updateByPrimaryKey(product);
           }
            return ResultModel.build(200, "取消订单成功");
        } else {
            return ResultModel.build(0, "取消订单失败");
        }

    }

    /**
     * 支付订单
     */
    @GetMapping("/order/pay/{id}")
    public String pay(@PathVariable("id") String id, HttpSession session, Model model) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        Order order = orderMapper.selectByPrimaryKey(Integer.valueOf(id));
        model.addAttribute("order", order);
        List<Shopping> shoppingList = shoppingMapper.selectByUserId(userInfo.getId());
        model.addAttribute("address", shoppingList);
        return "/customer/payInfo";
    }

    @PostMapping("/order/payFor")
    @ResponseBody()
    public ResultModel payFor(String orderId, String reciverName, String reciverPhone, String reciverAddress, HttpSession session) {
        System.out.println(reciverName);
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        System.out.println(userInfo);
        Shopping shopping1 = new Shopping();
        shopping1.setUserId(userInfo.getId());
        shopping1.setReceiverName(reciverName);
        shopping1.setReceiverPhone(reciverPhone);
        shopping1.setReceiverAddress(reciverAddress);
        int result = shoppingMapper.insertCart(shopping1);
        Order order = orderMapper.findOrderByUserIdAndOrderNo(userInfo.getId(), Long.valueOf(orderId));
        System.out.println(order);
        System.out.println(userInfo.getBalance());
        if (result > 0) {
            if (userInfo.getBalance().compareTo(order.getPayment().add(new BigDecimal("10"))) == 1) {
                userInfo.setBalance(userInfo.getBalance().subtract(order.getPayment().add(new BigDecimal("10"))));
                order.setStatus(Const.OrderStatusEnum.ORDER_PAYED.getCode());
                order.setPaymentTime(new Date());
                int bo = orderMapper.updateByPrimaryKey(order);
                int co = userInfoMapper.updateUserBySelectActive(userInfo);
                if (co > 0 && bo > 0) {
                    return ResultModel.build(200, "支付成功");
                }
                else {
                    return ResultModel.build(0, "余额不足，支付失败");
                }
            }
        }

        return ResultModel.build(0, "支付失败");
    }
}

