package com.neuedu.mapper;

import com.neuedu.pojo.Product;
import com.neuedu.pojo.ProductInsert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
@Mapper
public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int insert(Product record);

    int insertProduct(ProductInsert record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    List<Product> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Product record);

    /**
     * 更新商品
     */
    int updateProductKeySelective(Product product);

    /**
     *按照productId,productName查询
     */
    List<Product> findProductByProductIdAndProductName(@Param("productId")Integer productId,
                                                       @Param("productName") String productName);

    /**
     * 前台接口-搜索商品
     */
    List<Product>searchProduct(@Param("integerSet") Set<Integer> integerSet, @Param("keyword") String keyword);
}