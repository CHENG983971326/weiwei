package com.neuedu.service.impl;

import com.neuedu.mapper.CategoryMapper;
import com.neuedu.mapper.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductInsert;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.common.ResultModel;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
   @Autowired
   ProductMapper productMapper;
   @Autowired
   CategoryMapper categoryMapper;



    @Autowired
    ICategoryService categoryService;

   /**
    * 添加商品
    */
   @Override
   public ResultModel insertProduct(ProductInsert product) {
      int result=productMapper.insertProduct(product);
if (result>0){
    return ResultModel.build(200,"添加成功");
}else
    return ResultModel.build(0,"添加失败");

  }
    /**
     * 商品的上下架
     */
    @Override
    public ResultModel set_on_status(Product product) {
        product.setStatus(1);
        int result= productMapper.updateProductKeySelective(product);
        if (result>0){
            return new ResultModel(200,"ok","上架成功");
        }
        else {
            return new ResultModel(0,"no","上架失败");
        }
    }
    @Override
    public ResultModel set_out_status(Product product) {
        product.setStatus(2);
        int result= productMapper.updateProductKeySelective(product);
        if (result>0){
            return new ResultModel(200,"ok","下架成功");
        }
        else {
            return new ResultModel(0,"no","下架失败");
        }
    }


    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    private ProductDetailVO assembleProductDetailVO(Product product){

        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
       productDetailVO.setDetail(product.getDetail());
       productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
       productDetailVO.setName(product.getName());
       productDetailVO.setMainImage(product.getMainImage());
       productDetailVO.setId(product.getId());
       productDetailVO.setPrice(product.getPrice());
       productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
if (category!=null){
    productDetailVO.setParentCategoryId(category.getParentId());
}else {
    productDetailVO.setParentCategoryId(0);
}
       return productDetailVO;
    }

}
