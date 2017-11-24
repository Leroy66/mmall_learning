package com.mmall.dao;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int selectCartProductCheckedByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    int checkOrUnCheckProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") int checked);

    int selectCartProductCountByUserId(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}