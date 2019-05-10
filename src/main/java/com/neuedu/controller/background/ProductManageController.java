package com.neuedu.controller.background;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.mapper.ProductMapper;
import com.neuedu.mapper.UserInfoMapper;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductInsert;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import com.neuedu.common.ResultModel;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller

public class ProductManageController {
    @Autowired
    IProductService productService;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 删除商品
     */
    @RequestMapping("/manage/product/delete.do")
    @ResponseBody
    public ResultModel deleteProduct(HttpSession session, Product product) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
//        if (userInfo == null) {
//            map.put("msg", "当前无人登陆，请登录");
//            return "index";
//        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
//            map.put("error", "当前用户无此权限");
//            return "index";                                         //错误页面，无权限
//        } else {
            int result=productMapper.deleteByPrimaryKey(product.getId());
            if (result>0){
            return new ResultModel(200,"ok","删除成功");}
            else {return new ResultModel(0,"no","删除失败");}

        }
    /**
     * 产品的上下架
     */
    @RequestMapping(value = "/manage/product/status_on.do")
    @ResponseBody
    public ResultModel set_on_status(HttpSession session, Product product) {
           return productService.set_on_status(product);

    }
    @RequestMapping(value = "/manage/product/status_out.do")
    @ResponseBody
    public ResultModel set_out_status(HttpSession session, Product product) {
      return productService.set_out_status(product);
    }
   
    /**
     * 新增商品
     */
    @PostMapping("/manage/product/add")
    @ResponseBody()
    public ResultModel insertProd(String categoryId,String status,String name,String subtitle,
                                  String detail,String price,String stock,@RequestParam("fileinp") MultipartFile file)throws Exception {
      try {
          if (file != null) {
              System.out.println(stock);
              System.out.println(price);
              String licence = "E:\\file\\";
              // 生成文件新的名字
              byte[] fileBytes = file.getBytes();//文件字节流
              if (file != null && !file.equals("")) {
                  String originalFilename = file.getOriginalFilename();
                  System.out.println(originalFilename);
                  String mainImage = licence + originalFilename;//新文件名
                  ProductInsert product = new ProductInsert(Integer.valueOf(categoryId), Integer.valueOf(status),
                          new BigDecimal(price), name, subtitle, detail, Integer.valueOf(stock), mainImage);
                  System.out.println(product);
                  return productService.insertProduct(product);

              }
          }

      }catch (Exception e){
          e.printStackTrace();
      }
        return ResultModel.build(10, "文件未上传成功");
    }

    @RequestMapping(value = "/manage/product/add.do")
    public String toInsert(){
        return "/products/productAdd";
    }

    /**
     * 修改商品
     */
    @RequestMapping(value = "/manage/product/update.do/{id}")
    public String toUpdate(@PathVariable("id") Integer id,Model model){
        model.addAttribute("id",id);
       Product product=productMapper.selectByPrimaryKey(id);
       product.setMainImage(product.getMainImage().substring(2));
        model.addAttribute("product",product);
        return "/products/productUpdate";
    }

    @RequestMapping(value = "/manage/product/update")
    public String updateProd(HttpServletRequest request) {
        Integer id=Integer.valueOf(request.getParameter("id"));
        Integer categoryId=Integer.valueOf(request.getParameter("categoryid"));
        Integer stock=Integer.valueOf(request.getParameter("stock"));
        Integer status=Integer.valueOf(request.getParameter("status"));
        String name= request.getParameter("name");
        String subtitle=request.getParameter("subtitle");
        String detail= request.getParameter("detail");
        BigDecimal price= new BigDecimal(request.getParameter("price")) ;
        Product product= productMapper.selectByPrimaryKey(id);
        product.setCategoryId(categoryId);product.setStock(stock);product.setStatus(status);
        product.setName(name);product.setSubtitle(subtitle);product.setDetail(detail);product.setPrice(price);
        productMapper.updateByPrimaryKey(product);
        return "redirect:/manage/product/list.do";
    }

    /**
     * 管理员查看商品列表
     */
    @RequestMapping(value = "/manage/product/list.do")
    public String productList(HttpSession session, Map<String, Object> map) {
        String username = (String) session.getAttribute("loginuser");
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsername(username);
        if (userInfo == null) {
            map.put("msg", "当前无人登陆，请登录");
            return "index";
        } else if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            map.put("error", "当前用户无此权限");
            return "index";                                         //错误页面，无权限
        } else {
            return "/products/productList";
        }
   }
    @GetMapping("/manage/product/list")
    @ResponseBody()
    public List<ProductListVO> productList(){
        List<Product> productList = productMapper.selectAll();
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList != null && productList.size() > 0) {
            for (Product product : productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        return productListVOList;
    }

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

