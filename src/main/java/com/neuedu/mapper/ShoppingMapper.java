package com.neuedu.mapper;

import com.neuedu.pojo.Shopping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ShoppingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_shopping
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_shopping
     *
     * @mbg.generated
     */
    int insert(Shopping record);
    int insertCart(Shopping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_shopping
     *
     * @mbg.generated
     */
    Shopping selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_shopping
     *
     * @mbg.generated
     */
    List<Shopping> selectAll();

    List<Shopping> selectByUserId(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_shopping
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Shopping record);
    int updateBySelectiveKey(Shopping shopping);
    /**
     * 删除地址
     * @Param shoppingId and userId
     */
    int deleteByUserIdAndShoppingId(@Param("userId") Integer userId,
                                    @Param("shoppingId") Integer shoppingId);
}