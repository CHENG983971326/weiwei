package com.neuedu.mapper;

import com.neuedu.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
@Mapper
public interface CartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);
    BigDecimal findPayMentByUseIdAndChecked(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int insert(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    List<Cart> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_cart
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Cart record);
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId,
                                        @Param("productId") Integer productId);

    /**
     * 查询用户的购物车信息
     */
    List<Cart> selectCartByUserId(Integer userId);
    /**
     * 统计用户购物信息是否全选
     * 返回值大于0，说明为未全选
     */
    int isCheckedAll(Integer userId);

    /**
     * 删除购物车中某些商品
     */
    int deleteByUserIdAndProductIds(@Param("userId") Integer userId,@Param("productIdList") List<Integer>productIdList);

    /**
     * 操作购物车商品是否选中
     * @param userId
     * @param productId
     * @param check 1,选中 0,不选中
     */
    int selectOrUnselectProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("check")
            Integer check);

    /**
     * 统计用户购物车中产品的数量
     */
    int get_cart_product_count(Integer userId);

    /**
     * 查询购物车中用户已经选中的商品
     */
    List<Cart> findCartListByUseIdAndChecked(Integer userId);
    /**
     * 批量删除购物车中商品
     */
    int batchDelete(List<Cart> cartList);
}