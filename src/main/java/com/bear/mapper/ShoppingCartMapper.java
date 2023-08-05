package com.bear.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bear.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
