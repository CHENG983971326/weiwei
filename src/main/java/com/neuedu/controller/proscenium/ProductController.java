package com.neuedu.controller.proscenium;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ResultModel;
import com.neuedu.mapper.CartMapper;
import com.neuedu.mapper.ProductMapper;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {
    @Autowired
    IProductService productService;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CartMapper cartMapper;
//    /**
//     * 前台-商品详情
//     */
//    @RequestMapping(value = "/detail.do")
//    public ServerResponse detail(Integer productId){
//
//        return productService.detail_portal(productId);
//    }
     /**
     * 消费者查看商品列表
     */
    @RequestMapping(value = "/customer/product/list.do")
    public String productList() {
        //查询商品数据
            return "/products/customerProduct";
    }
        @GetMapping(value = "/customer/product/list")
        @ResponseBody
        public List<ProductListVO> productListVOS(){

            List<Product> productList = productMapper.selectAll();
            List<ProductListVO> productListVOList = Lists.newArrayList();
            if (productList != null && productList.size() > 0) {
                for (Product product : productList) {
                    if (product.getStatus()== Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                        ProductListVO productListVO = assembleProductListVO(product);
                        productListVOList.add(productListVO);}
                }
            }
            System.out.println(productListVOList);
            return productListVOList;
        }
    /**
     * 消费者查看商品详情
      */
    @GetMapping (value = "/customer/product/query.do/{id}")
    public String queryProduct(@PathVariable("id") String id,Model model){
       Product product= productMapper.selectByPrimaryKey(Integer.valueOf(id));
        String imgUrl="/file/"+product.getMainImage().substring(8);
        product.setMainImage(imgUrl);
       model.addAttribute("product",product);
       return "/customer/productDetail";
            }
    /**
     * 消费者添加商品到购物车
     */
    @RequestMapping (value = "/customer/product/add.do/{id}/{name}/{price}")
    public String toBuyCar(@PathVariable("id") String id,@PathVariable("name") String name,
                           @PathVariable("price") String price,  Model model){
        Product product=productMapper.selectByPrimaryKey(Integer.valueOf(id));
        String imgUrl="/file/"+product.getMainImage().substring(8);
        product.setMainImage(imgUrl);
        model.addAttribute("id",id);model.addAttribute("product",product);
        model.addAttribute("name",name); model.addAttribute("price",Double.valueOf(price));model.addAttribute("stock",product.getStock());
        return "/products/customerAddPro";
    }
    @GetMapping(value = "/customer/product/add")
    @ResponseBody
    public ResultModel addToCart(String id,String buyCount, HttpSession session){
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        System.out.println(userInfo);
//        Integer id=Integer.valueOf(request.getParameter("id"));
        Product product=productMapper.selectByPrimaryKey(Integer.valueOf(id));
        System.out.println(product);
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userInfo.getId(),Integer.valueOf(id));
        if (cart!=null){
            cart.setQuantity(cart.getQuantity()+Integer.valueOf(buyCount));
//            BigDecimal a=cart.getTotalPrice();
//            BigDecimal b=new BigDecimal(buyCount).multiply(product.getPrice());
//             a=a.add(product.getPrice().multiply(new BigDecimal(buyCount)));
            System.out.println(cart.getTotalPrice());
            cart.setTotalPrice(cart.getTotalPrice().add(new BigDecimal(buyCount).multiply(product.getPrice())));
            System.out.println(cart.getTotalPrice());
            System.out.println(cart.getTotalPrice().add(new BigDecimal(buyCount).multiply(product.getPrice())));
            int count=cartMapper.updateByPrimaryKey(cart);
            if (count>0){
                product.setStock(product.getStock()-Integer.valueOf(buyCount));
                productMapper.updateByPrimaryKey(product);
                return ResultModel.build(200,"加入成功");
            }
            else return ResultModel.build(0,"加入失败");
        }
//        Integer count=Integer.valueOf(request.getParameter("buycount")) ;
        int result=cartMapper.insert(new Cart(userInfo.getId(),product.getId(),Integer.valueOf(buyCount),Const.CartCheckedEnum.PRODUCT_CHECKED.getCode(),new BigDecimal(buyCount).multiply(product.getPrice())));
        if (result>0){
            product.setStock(product.getStock()-Integer.valueOf(buyCount));
            session.setAttribute("productId",id);
            session.setAttribute("count",buyCount);
            productMapper.updateByPrimaryKey(product);
            return ResultModel.build(200,"加入成功");
        }
        else {
            return ResultModel.build(0,"加入失败");
        }
    }
    /**
     *
     * @param product
     * @return
     */

    private ProductListVO assembleProductListVO (Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setDetail(product.getDetail());
        return productListVO;
    }
}







