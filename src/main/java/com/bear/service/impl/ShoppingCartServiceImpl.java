package com.bear.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bear.entity.ShoppingCart;
import com.bear.mapper.ShoppingCartMapper;
import com.bear.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
