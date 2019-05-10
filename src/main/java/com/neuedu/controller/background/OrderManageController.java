package com.neuedu.controller.background;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.mapper.OrderItemMapper;
import com.neuedu.mapper.OrderMapper;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import com.neuedu.common.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class OrderManageController {
    @Autowired
    IOrderService orderService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;

    /**
     * 查看订单列表
     */
    @RequestMapping(value = "/manage/order/list")
    public String userList(HttpSession session, Map<String,Object> map) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo == null) {
            map.put("msg", "当前无人登陆，请登录");
            return "index";
        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            map.put("error", "当前用户无此权限");
            return "index";                                         //错误页面，无权限
        } else {
            return "/order/orderList";
        }
    }
    @GetMapping("/manage/order/lists")
    @ResponseBody()
    public List<Order> orderList(){
        List<Order> list = orderMapper.selectAll();
        return list;
    }
    /**
     * 查看订单详情列表
     */
    @RequestMapping(value = "/manage/orderitem/list")
    public String orderItemList(HttpSession session, Map<String,Object> map) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo == null) {
            map.put("msg", "当前无人登陆，请登录");
            return "index";
        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            map.put("error", "当前用户无此权限");
            return "index";                                         //错误页面，无权限
        } else {
            return "/order/orderItemList";
        }
    }
    @GetMapping("/manage/orderitem/lists")
    @ResponseBody()
    public List<OrderItem> orderItemLists(){
        List<OrderItem> list = orderItemMapper.selectAll();
        return list;
    }
    /**
     * 管理员删除订单
     */
    @RequestMapping("/manage/order/delete.do")
    @ResponseBody
    public ResultModel deleteOrder(HttpSession session, Order order) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo=userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return new ResultModel(1,"no","无此权限");
        }
        orderMapper.deleteByPrimaryKey(order.getId());
        int result=orderItemMapper.deleteByPrimaryKey(order.getId());
        if (result>0){
            return new ResultModel(200,"ok","删除成功");}
        else {return new ResultModel(0,"no","删除失败");}
    }
    /**
     * 管理员删除订单详情
     */
    @RequestMapping("/manage/orderitem/delete.do")
    @ResponseBody
    public ResultModel deleteOrderItem(HttpSession session, OrderItem orderItem){
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo=userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode()){
            return new ResultModel(1,"no","无此权限");
        }
        orderMapper.deleteByPrimaryKey(orderItem.getId());
        int result=orderItemMapper.deleteByPrimaryKey(orderItem.getId());
        if (result>0){
            return new ResultModel(200,"ok","删除成功");}
        else {return new ResultModel(0,"no","删除失败");}
    }
    /**
     * 订单信息回显
     */
    @RequestMapping (value = "/manage/order/update.do/{id}")
    public String toUpdate(@PathVariable("id") Integer orderId, Model model){
        model.addAttribute("orderId",orderId);

        return "/order/orderUpdate";
    }


    /**
     * 管理员更改订单
     */
    @RequestMapping(value = "/manage/orderstatus/update.do")
    public String updateOr(Model model){
       List<Order>list= orderMapper.selectAll();
       model.addAttribute("lists",list);

        return "/order/ordersupdate";
    }
    @RequestMapping(value = "/manage/orderstatus/update")
    public String updateOrders(HttpServletRequest request){
        Integer orderId=Integer.valueOf(request.getParameter("orderId"));
        Integer status=Integer.valueOf(request.getParameter("status"));
        Order order= orderMapper.selectByPrimaryKey(orderId);
        order.setStatus(status);
        orderMapper.updateByPrimaryKey(order);
        return "redirect:/manage/order/list";
    }
    @RequestMapping(value="/manage/ordersupdate/getName")
    @ResponseBody
    public ResultModel getOrdersId(Long orderNo){
        Order order=orderMapper.findOrderByOrderNo(orderNo);
        if (order.getUserId()!=null){
            return ResultModel.build(200,"成功",order.getUserId());
        }else {
            return ResultModel.build(0,"失败",null);
        }
    }
    /**
     * 管理员编辑订单
     */
    @RequestMapping(value = "/manage/order/update")
    public String updateOrder(HttpServletRequest request){

        Integer orderId=Integer.valueOf(request.getParameter("orderNo"));
        Integer status=Integer.valueOf(request.getParameter("status"));
        Order order= orderMapper.selectByPrimaryKey(orderId);
        order.setStatus(status);
        orderMapper.updateByPrimaryKey(order);
            return "redirect:/manage/order/list";
    }

//    /**
//     * 取消订单
//     * */
//    @RequestMapping(value="/send_goods.do")
//    public ServerResponse send_goods(HttpSession session, Long orderNo){
//        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CURRENTUSER);
//        if (userInfo==null){
//            return ServerResponse.serverResponseByError("需要登录");
//        }
//
//        return orderService.send_goods(userInfo.getId(),orderNo);
//    }

}

